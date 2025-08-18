package dev.ivanov.egrz.bot.entities;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "opinions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Opinion {
  
  @Id
  private String key;

  private String expertiseResultId;

  private String expertiseNumber;
  
  private ZonedDateTime expertiseConclusionDate;

  @Column(length = 2000)
  private String expertiseResultType;

  @Column(length = 2000)
  private String expertiseType;

  @Column(length = 2000)
  private String expertiseDocumentType;

  private String subjectRf;

  private Integer subjectRfCode;

  @Column(length = 2000)
  private String expertiseObjectNameAndAddress;

  @Column(length = 2000)
  private String expertiseObjectName;

  @Column(length = 2000)
  private String expertiseObjectAddress;

  @Column(length = 2000)
  private String expertiseOrganizatioInfo;

  @Column(length = 2000)
  private String expertiseOrganizatioINN;

  @Column(length = 2000)
  private String expertiseOrganizatioKPP;

  @Column(length = 2000)
  private String expertiseOrganizatioOGRN;

  @Column(length = 2000)
  private String expertiseOrganizatioAddress;
  
  @Column(length = 2000)
  private String developerAndTechnicalCustomerOrganizationInfo;
  
  @Column(length = 2000)
  private String developerOrganizationInfo;

  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "developer_organization", length = 2000)
  @CollectionTable(name = "developers", joinColumns = @JoinColumn(name = "opinion_id"))
  private List<String> developerOrganizations;

  @Column(length = 2000)
  private String technicalCustomerOrganizationInfo;

  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "technical_customer_organization", length = 2000)
  @CollectionTable(name = "technical_customer_organizations", joinColumns = @JoinColumn(name = "opinion_id"))
  private List<String> technicalCustomerOrganizations;

  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "developer_and_technical_customer_organization", length = 2000)
  @CollectionTable(name = "developer_and_technical_customer_organizations", joinColumns = @JoinColumn(name = "opinion_id"))
  private List<String> developerAndTechnicalCustomerOrganizations;

  @Column(length = 2000)
  private String plannerOrganizationInfo;

  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "planner_organization", length = 2000)
  @CollectionTable(name = "planner_organizations", joinColumns = @JoinColumn(name = "opinion_id"))
  private List<String> plannerOrganizations;

  @Column(length = 1000)
  private String previousExpretiseResults;

  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "previous_expretise_results", length = 2000)
  @CollectionTable(name = "previous_expretise_results", joinColumns = @JoinColumn(name = "opinion_id"))
  private List<String> previousExpertiseResultsList;
  
  @Column(length = 2000)
  private String economyEfficiencyInfo;

  @Column(length = 2000)
  private String efficiencyOrderNumber;

  private ZonedDateTime efficiencyOrderNumberDate;

  private Boolean isEconomyEfficiency;

  @Column(length = 2000)
  private String tpr;

  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "tpr", length = 2000)
  @CollectionTable(name = "tpr", joinColumns = @JoinColumn(name = "opinion_id"))
  private List<String> tprList;

  private Boolean isTpr;

  private ZonedDateTime expertiseDate;
  
  @Column(length = 2000)
  private String workType;
}
