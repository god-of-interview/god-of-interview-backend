package com.capstone.godofinterview.domain.job.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobCategory {

    MANAGEMENT_ACCOUNTING_OFFICE("경영·회계·사무"),
    FINANCE_INSURANCE("금융·보험"),
    EDUCATION("교육"),
    MEDICAL_PHARMACEUTICAL_WELFARE("의료·제약·복지"),
    CULTURE_ART_DESIGN("문화·예술·디자인"),
    SERVICE("서비스"),
    CONSTRUCTION("건설"),
    MACHINERY_NEW_MATERIALS("기계·신소재"),
    TEXTILE_CLOTHING("섬유·의복"),
    ELECTRICAL_ELECTRONICS("전기·전자"),
    IT_DATA("IT·데이터"),
    FOOD("식품"),
    CHEMICAL_ENERGY_ENVIRONMENT("화학·에너지·환경"),
    WOOD_FURNITURE_PAPER("목재·가구·제지"),
    SALES_MARKETING_DISTRIBUTION_TRADE("판매·영업·유통·무역"),
    POLITICS("정치");

    private final String displayName;
}
