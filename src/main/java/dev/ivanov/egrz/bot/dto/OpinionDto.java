package dev.ivanov.egrz.bot.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ivanov.egrz.bot.entities.Opinion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class OpinionDto {
  @JsonProperty("Key")
  private String key;
  @JsonProperty("ExpertiseResultId")
  private String expertiseResultId;
  @JsonProperty("ExpertiseNumber")
  private String expertiseNumber;
  @JsonProperty("ExpertiseConclusionDate")
  private ZonedDateTime expertiseConclusionDate;
  @JsonProperty("ExpertiseResultType")
  private String expertiseResultType;
  @JsonProperty("ExpertiseType")
  private String expertiseType;
  @JsonProperty("ExpertiseDocumentType")
  private String expertiseDocumentType;
  @JsonProperty("SubjectRf")
  private String subjectRf;
  @JsonProperty("SubjectRfCode")
  private Integer subjectRfCode;
  @JsonProperty("ExpertiseObjectNameAndAddress")
  private String expertiseObjectNameAndAddress;
  @JsonProperty("ExpertiseObjectName")
  private String expertiseObjectName;
  @JsonProperty("ExpertiseObjectAddress")
  private String expertiseObjectAddress;
  @JsonProperty("ExpertiseOrganizatioInfo")
  private String expertiseOrganizatioInfo;
  @JsonProperty("ExpertiseOrganizatioINN")
  private String expertiseOrganizatioINN;
  @JsonProperty("ExpertiseOrganizatioKPP")
  private String expertiseOrganizatioKPP;
  @JsonProperty("ExpertiseOrganizatioOGRN")
  private String expertiseOrganizatioOGRN;
  @JsonProperty("ExpertiseOrganizatioAddress")
  private String expertiseOrganizatioAddress;
  @JsonProperty("DeveloperAndTechnicalCustomerOrganizationInfo")
  private String developerAndTechnicalCustomerOrganizationInfo;
  @JsonProperty("DeveloperOrganizationInfo")
  private String developerOrganizationInfo;
  @JsonProperty("DeveloperOrganizations")
  private List<String> developerOrganizations;
  @JsonProperty("TechnicalCustomerOrganizationInfo")
  private String technicalCustomerOrganizationInfo;
  @JsonProperty("TechnicalCustomerOrganizations")
  private List<String> technicalCustomerOrganizations;
  @JsonProperty("DeveloperAndTechnicalCustomerOrganizations")
  private List<String> developerAndTechnicalCustomerOrganizations;
  @JsonProperty("PlannerOrganizationInfo")
  private String plannerOrganizationInfo;
  @JsonProperty("PlannerOrganizations")
  private List<String> plannerOrganizations;
  @JsonProperty("PreviousExpretiseResults")
  private String previousExpretiseResults;
  @JsonProperty("PreviousExpertiseResultsList")
  private List<String> previousExpertiseResultsList;
  @JsonProperty("EconomyEfficiencyInfo")
  private String economyEfficiencyInfo;
  @JsonProperty("EfficiencyOrderNumber")
  private String efficiencyOrderNumber;
  @JsonProperty("EfficiencyOrderNumberDate")
  private ZonedDateTime efficiencyOrderNumberDate;
  @JsonProperty("IsEconomyEfficiency")
  private Boolean isEconomyEfficiency;
  @JsonProperty("Tpr")
  private String tpr;
  @JsonProperty("TprList")
  private List<String> tprList;
  @JsonProperty("IsTpr")
  private Boolean isTpr;
  @JsonProperty("ExpertiseDate")
  private ZonedDateTime expertiseDate;
  @JsonProperty("WorkType")
  private String workType;


  public Opinion mapToEntity() {
    Class<Opinion> opinionClass = Opinion.class;

    Class<OpinionDto> opinionDtoClass = OpinionDto.class;

    Map<String, Object> values = new HashMap<>();

    Method[] opinionDtoMethods = opinionDtoClass.getDeclaredMethods();

    for (Method method: opinionDtoMethods) {
      if (method.getName().startsWith("get")) {
        try {
          Object get = method.invoke(this);
          if (get instanceof String) {
            if (((String)get).length() >= 2000) 
            get = ((String)get).substring(0, 1990) + "...";
          }
          else if (get instanceof List) {
            for (int i = 0; i < ((List) get).size(); i++) {
              Object o = ((List) get).get(i);
              if (o instanceof String)
              if (((String) o).length() >= 2000)
              ((List) get).set(i, ((String) o).substring(0, 1990) + "...");
            }
          }
          values.put(method.getName().substring(3), get);
        } catch (IllegalAccessException|InvocationTargetException e) {
          log.error("reflection method call exception", e);
        }
      }
    }
    
    Opinion result = new Opinion();
    Method[] opinionMethods = opinionClass.getDeclaredMethods();
    for (Method method: opinionMethods) {
      if (method.getName().startsWith("set")) {
        try {
          Object[] args = {values.get(method.getName().substring(3))};
          method.invoke(result, args);
        } catch (IllegalAccessException|InvocationTargetException e) {
          log.error("reflection method call exception", e);
        }
      }
    }

    return result;
  }

}
