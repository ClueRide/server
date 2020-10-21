package com.clueride.network.edge.upload;

import javax.ws.rs.FormParam;
import java.io.InputStream;

public class EdgeUploadRequest {
    @FormParam("fileData") private InputStream fileData;

    public InputStream getFileData() {
        return fileData;
    }

    public EdgeUploadRequest withFileData(InputStream fileData) {
        this.fileData = fileData;
        return this;
    }

}
