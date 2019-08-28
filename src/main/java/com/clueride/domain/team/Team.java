/**
 * Copyright 2015 Jett Marks
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created 11/17/15.
 */
package com.clueride.domain.team;

import java.util.List;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.domain.account.member.Member;
import static java.util.Objects.requireNonNull;

@Immutable
public class Team {
    private final Integer id;
    private final String name;
    private final List<Member> members;

    public Team(TeamEntity builder) {
        this.id = requireNonNull(builder.getId());
        this.name = requireNonNull(builder.getName());
        this.members = requireNonNull(builder.getMembers());
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void add(Member member) {
        members.add(member);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
