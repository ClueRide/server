package com.clueride.domain.account.badgeos;

import com.clueride.auth.identity.ClueRideIdentity;
import com.clueride.domain.account.member.MemberEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Represents a User Record in the BadgeOS system: `wp_users` table.
 *
 * This is the main record. There are also attributes that are stored in
 * the `wp_usermeta` table that share the `user_id`.
 */
@Entity
@Table(name = "wp_users")
public class BadgeOsUserEntity {
    private static final String DEFAULT_CAPABILITIES = "a:1:{s:10:\"subscriber\";b:1;}";
    private static final String DEFAULT_USER_LEVEL = "0";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="user_login")
    private String login;
    @Column(name="user_pass")
    private String password;
    @Column(name="user_nicename")
    private String niceName;
    @Column(name="user_email")
    private String email;
    @Column(name="user_url")
    private String url;
    @Column(name="user_registered")
    private Date registered;
    @Column(name="user_status")
    private int status = 0;
    @Column(name="display_name")
    private String displayName;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "badgeOsUserEntity"
    )
    private List<UserAttributeEntity> userAttributes;

    /* May not need this. */
//    public BadgeOsUser build() {
//        return new BadgeOsUser(this);
//    }

    public static BadgeOsUserEntity builder() {
        return new BadgeOsUserEntity();
    }

    public static BadgeOsUserEntity from(
            MemberEntity memberEntity,
            ClueRideIdentity clueRideIdentity
    ) {
        requireNonNull(memberEntity, "Member Entity is required");

        BadgeOsUserEntity badgeOsUserEntity = builder()
                .withId(memberEntity.getBadgeOSId())
                .withDisplayName(memberEntity.getDisplayName())
                .withEmail(memberEntity.getEmailAddress())
                // TODO SVR-110: Sort out the Logon which should be unique and preferably not the email address.
                .withLogin(memberEntity.getEmailAddress())
                // TODO SVR-110: Sort out the nicename which is used for URL building
                .withNiceName("something")
                .withPassword("1qaz2wsx!QAZ@WSX")
                .withRegistered(new Date(new java.util.Date().getTime()))
                .withUrl("");


        badgeOsUserEntity.withUserAttributes(
                buildUserAttributeList(
                        badgeOsUserEntity,
                        clueRideIdentity
                )
        );

        return badgeOsUserEntity;
    }

    private static List<UserAttributeEntity> buildUserAttributeList(
            BadgeOsUserEntity badgeOsUserEntity,
            ClueRideIdentity clueRideIdentity
    ) {
        List<UserAttributeEntity> userAttributeEntities = new ArrayList<>();
        userAttributeEntities.add(
                getAttributeEntity(
                        UserAttributeKey.FIRST_NAME,
                        clueRideIdentity.getGivenName()
                ).withBadgeOsUserEntity(badgeOsUserEntity)
        );

        userAttributeEntities.add(
                getAttributeEntity(
                        UserAttributeKey.LAST_NAME,
                        clueRideIdentity.getFamilyName()
                ).withBadgeOsUserEntity(badgeOsUserEntity)
        );

        userAttributeEntities.add(
                getAttributeEntity(
                        UserAttributeKey.WP_CAPABILITIES,
                        DEFAULT_CAPABILITIES
                ).withBadgeOsUserEntity(badgeOsUserEntity)
        );

        userAttributeEntities.add(
                getAttributeEntity(
                        UserAttributeKey.WP_USER_LEVEL,
                        DEFAULT_USER_LEVEL
                ).withBadgeOsUserEntity(badgeOsUserEntity)
        );

        userAttributeEntities.add(
                getAttributeEntity(
                        UserAttributeKey.OA_SOCIAL_LOGIN_PICTURE,
                        clueRideIdentity.getPictureUrl().toString()
                ).withBadgeOsUserEntity(badgeOsUserEntity)
        );

        return userAttributeEntities;
    }

    private static UserAttributeEntity getAttributeEntity(String key, String value) {
        return UserAttributeEntity.builder()
                .withUserAttributeKey(key)
                .withUserAttributeValue(value);
    }

    public Integer getId() {
        return id;
    }

    public BadgeOsUserEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public BadgeOsUserEntity withLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public BadgeOsUserEntity withPassword(String password) {
        this.password = password;
        return this;
    }

    public String getNiceName() {
        return niceName;
    }

    public BadgeOsUserEntity withNiceName(String niceName) {
        this.niceName = niceName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public BadgeOsUserEntity withEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * This is NOT the image URL; it's the user's website URL.
     * @return String representation of user's website.
     */
    public String getUrl() {
        return url;
    }

    /**
     * This is NOT the image URL; it's the user's website URL.
     * @param url String representation of user's website.
     * @return this.
     */
    public BadgeOsUserEntity withUrl(String url) {
        this.url = url;
        return this;
    }

    public Date getRegistered() {
        return registered;
    }

    public BadgeOsUserEntity withRegistered(Date registered) {
        this.registered = registered;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BadgeOsUserEntity withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public List<UserAttributeEntity> getUserAttributes() {
        return userAttributes;
    }

    public BadgeOsUserEntity withUserAttributes(List<UserAttributeEntity> userAttributes) {
        this.userAttributes = userAttributes;
        return this;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
