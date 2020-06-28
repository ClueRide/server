/*
 * Copyright 2019 Jett Marks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by jett on 2/10/19.
 */
package com.clueride.domain.image;

import com.clueride.config.ConfigService;
import org.apache.commons.io.IOUtils;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * JPA implementation of linking Image record to Location record
 * and File-System implementation of persisting image stream to
 * Image Web Server.
 */
public class ImageStoreImpl implements ImageStore {
    private final Map<Integer,File> locationDirectories = new HashMap<>();

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Inject
    private ConfigService config;

    @Override
    public ImageLinkEntity getImageLink(int imageId) {
        return entityManager.find(ImageLinkEntity.class, imageId);
    }

    @Override
    public List<ImageLinkEntity> getImagesForLocation(Integer locationId) {
        return entityManager
                .createQuery(
                        "select i from ImageLinkEntity i, ImageByLocationEntity l" +
                                " where i.id = l.imageId" +
                                "   and l.locationId = :locationId",
                        ImageLinkEntity.class
                )
                .setParameter("locationId", locationId)
                .getResultList();
    }

    @Override
    public Integer addNew(Integer locationId, InputStream imageData) {
        Integer newSequenceId = 1;
        File newImageFile;

        // Make sure only one thread at a time is updating the list of image files
        synchronized (locationDirectories) {
            File locDir = findOrCreateLocationDirectory(locationId);

            /* Find max ID from the files in that directory. */
            for (File imageFile : requireNonNull(locDir.listFiles())) {
                Integer existingSeqId = getIdFromFileName(imageFile);
                if (newSequenceId <= existingSeqId) {
                    newSequenceId = existingSeqId+1;
                }
            }

            String newFileName = locDir.getPath() + File.separator + newSequenceId + ".jpg";
            newImageFile = new File(newFileName);
        }

        // Ready to write the file
        try {
            FileOutputStream out = new FileOutputStream(newImageFile);
            IOUtils.copy(imageData, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newSequenceId;
    }

    private File findOrCreateLocationDirectory(Integer locationId) {
        File locDir = locationDirectories.get(locationId);
        if (locationDirectories.containsKey(locationId)) {
            if (locDir.isDirectory()) {
                /* We have read this directory, and it can still be used. */
                return locDir;
            }
        }
        /* See if the directory exists on the file system and ... */
        File baseDir = new File(config.getImageBaseDirectory());
        locDir = new File(baseDir + File.separator + locationId);

        /* ... if not, create it. */
        if (!locDir.isDirectory()) {
            locDir.mkdirs();
        }
        locationDirectories.put(locationId, locDir);
        return locDir;
    }

    private Integer getIdFromFileName(File imageFile) {
        return Integer.parseInt(imageFile.getName().split("\\.")[0]);
    }

    @Override
    public Integer addNewToLocation(ImageLinkEntity imageLinkEntity, Integer locationId) {
        try {
            userTransaction.begin();
            entityManager.persist(imageLinkEntity);
            ImageByLocationEntity imageByLocationEntity = new ImageByLocationEntity();
            imageByLocationEntity.setImageId(imageLinkEntity.getId());
            imageByLocationEntity.setLocationId(locationId);
            entityManager.persist(imageByLocationEntity);
            userTransaction.commit();
        } catch (NotSupportedException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
            e.printStackTrace();
        }
        return imageLinkEntity.getId();
    }

    @Override
    public void releaseImagesForLocation(Integer locationId) {
        try {
            userTransaction.begin();
            entityManager.createQuery("DELETE from ImageByLocationEntity where locationId = :locationId")
                    .setParameter("locationId", locationId)
                    .executeUpdate();
            userTransaction.commit();
        } catch (NotSupportedException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
            e.printStackTrace();
        }
    }

}
