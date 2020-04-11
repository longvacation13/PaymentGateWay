--
-- 회원
--
DROP TABLE IF EXISTS MEMBER;

CREATE TABLE MEMBER COMMENT '회원' (
    MBR_ID      VARCHAR(10)     NOT NULL    COMMENT '회원ID'
  , NAME        VARCHAR(100)                COMMENT '회원명'
  , PRIMARY KEY (MBR_ID)
);
-- 유일한 사용처인 회원정보 조회시 MBR_ID(cluster_idx) 기반이기 때문에 인덱스 생성하지 않음  


--
-- 결제마스터 - 신용카드, 핸드폰, 계좌이체 등
--
DROP TABLE IF EXISTS PAYMENT_MST;

CREATE TABLE PAYMENT_MST COMMENT '결제마스터' (
    PMT_CODE        VARCHAR(5)      NOT NULL    COMMENT '결제코드'
  , PMT_TYPE        VARCHAR(4)                  COMMENT '결제타입'
  , PMT_NAME        VARCHAR(100)                COMMENT '결제코드명'
  , PART_CNCL_YN    VARCHAR(1)                  COMMENT '부분취소가능여부'
);
-- 마스터성 코드이고 몇개 안되기 때문에 인덱스 생성하지 않음


--
-- 결제내역
--
DROP TABLE IF EXISTS PAYMENT;

CREATE TABLE PAYMENT COMMENT '결제내역' (
    PMT_ID          VARCHAR(10)     NOT NULL    COMMENT '결제ID'
  , MBR_ID          VARCHAR(10)     NOT NULL    COMMENT '회원ID'
  , PMT_CODE        VARCHAR(5)      NOT NULL    COMMENT '결제코드'
  , PMT_TYPE        VARCHAR(4)                  COMMENT '결제타입'
  , SUCC_YN         VARCHAR(1)                  COMMENT '성공여부'
  , SUCC_MSG        VARCHAR(100)                COMMENT '성공메세지'
  , APRV_TYPE       VARCHAR(2)                  COMMENT '승인타입'
  , APRV_TIME       TIMESTAMP                   COMMENT '승인일시'
  , PMT_AMT         BIGINT                      COMMENT '결제금액'
  , PRIMARY KEY (PMT_ID)
);


-- 1. 결제정보조회 용 인덱스 생성  ( 취소, 부분취소시 결제정보 조회 ) 
CREATE INDEX IDX_PMT_ID_MBR_ID_PAYMENT ON PAYMENT ( PMT_ID, MBR_ID );

-- 2. getRecentPaymentList 용 인덱스 - h2 database index skip scan 불가한 것으로 보여 추가함
CREATE INDEX IDX_PMT_ID_SUCC_YN_PAYMENT ON PAYMENT ( MBR_ID, SUCC_YN );





