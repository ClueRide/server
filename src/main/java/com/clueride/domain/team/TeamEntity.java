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
 * Created by jett on 1/13/19.
 */
package com.clueride.domain.team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberEntity;

/**
 * Persistable Builder for {@link Team} instances.
 */
@Entity
@Table(name = "team")
public final class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_pk_sequence")
    @SequenceGenerator(name = "team_pk_sequence", sequenceName = "team_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    /* Without FetchType.EAGER, transaction ended before records were pulled. */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "team_membership",
            joinColumns = {@JoinColumn(name = "team_id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id")}
    )
    private Set<MemberEntity> memberEntities = new HashSet<>();

    /* Allows Jackson to construct. */
    public TeamEntity() {
    }

    /* Normal pattern for constructing a Builder. */
    public static TeamEntity builder() {
        return new TeamEntity();
    }

    public Team build() {
        return new Team(this);
    }

    public Integer getId() {
        return id;
    }

    public TeamEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TeamEntity withName(String name) {
        this.name = name;
        return this;
    }

    public List<Member> getMembers() {
        List<Member> members = new ArrayList<>();
        for (MemberEntity builder : memberEntities) {
            members.add(builder.build());
        }
        return members;
    }

    public TeamEntity withMembers(Set<MemberEntity> memberEntities) {
        this.memberEntities = memberEntities;
        return this;
    }

    public TeamEntity withNewMember(Member newMember) {
        this.memberEntities.add(MemberEntity.from(newMember));
        return this;
    }
}
