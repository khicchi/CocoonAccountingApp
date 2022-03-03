package com.cocoon.util.payment_old_one;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value={"hibernate_Lazy_Initializer"}, ignoreUnknown = true)
@ToString
public class AuthorizationResponse {

        Meta MetaObject;
        Data DataObject;


        // Getter Methods

        public Meta getMeta() {
            return MetaObject;
        }

        public Data getData() {
            return DataObject;
        }

        // Setter Methods

        public void setMeta(Meta metaObject) {
            this.MetaObject = metaObject;
        }

        public void setData(Data dataObject) {
            this.DataObject = dataObject;
        }
    }


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value={"hibernate_Lazy_Initializer"}, ignoreUnknown = true)
@ToString
    class Data {
        private String id;
        private String userUuid;
        private String applicationUserId;
        private String institutionId;
        private String status;
        private String createdAt;
        ArrayList< Object > featureScope = new ArrayList < Object > ();
        private String state;
        private String institutionConsentId;
        private String authorisationUrl;
        private String qrCodeUrl;


        // Getter Methods

        public String getId() {
            return id;
        }

        public String getUserUuid() {
            return userUuid;
        }

        public String getApplicationUserId() {
            return applicationUserId;
        }

        public String getInstitutionId() {
            return institutionId;
        }

        public String getStatus() {
            return status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getState() {
            return state;
        }

        public String getInstitutionConsentId() {
            return institutionConsentId;
        }

        public String getAuthorisationUrl() {
            return authorisationUrl;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        // Setter Methods

        public void setId(String id) {
            this.id = id;
        }

        public void setUserUuid(String userUuid) {
            this.userUuid = userUuid;
        }

        public void setApplicationUserId(String applicationUserId) {
            this.applicationUserId = applicationUserId;
        }

        public void setInstitutionId(String institutionId) {
            this.institutionId = institutionId;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setInstitutionConsentId(String institutionConsentId) {
            this.institutionConsentId = institutionConsentId;
        }

        public void setAuthorisationUrl(String authorisationUrl) {
            this.authorisationUrl = authorisationUrl;
        }

        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }
    }


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value={"hibernate_Lazy_Initializer"}, ignoreUnknown = true)
@ToString
    class Meta {
        private String tracingId;


        // Getter Methods

        public String getTracingId() {
            return tracingId;
        }

        // Setter Methods

        public void setTracingId(String tracingId) {
            this.tracingId = tracingId;
        }
}
