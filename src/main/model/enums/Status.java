package model.enums;

// Represents a list of all the states an application for a volunteer can be in
// An applicant Volunteer starts off with NA status then is assigned a PENDING status after applying.
// Finally, the status changes to ACCEPTED once the founder of the organization accepts the applicant

public enum Status {
    NA, PENDING, ACCEPTED
}
