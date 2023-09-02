# Process-Scheduling-Simulator 
Java와 JavaFX를 이용한 CPU 프로세스 스케줄링 시뮬레이터.

# UI
![Simulator](https://github.com/refracta/Process-Scheduling-Simulator/assets/58779799/2c951869-0dc0-47e2-83e5-880c6b3c5c1c)

## ①
코어와 스케줄링 기법 및 스케줄링 별 필요 세부사항을 설정한다.
- 성능 코어(P코어) 수 설정
- 효율 코어(E코어) 수 설정
- 성능 코어 상한 수와 효율 코어 상한 수 설정을 통한 랜덤 코어 수 설정
- 스케줄링 기법 설정
- Time quantum 설정 - RR(Round-Robin), RR2Q(Round-Robin with Run queue, Ready queue), GMRL(Give More Rice cake to Loser)에 필요하다.
- Run queue(실행 큐) 내의 작업 개수 설정 - RR2Q에 필요하다.
- 플래그 상한 설정 - GMRL에 필요하다.

## ②
프로세서와 프로세스의 정보를 볼 수 있다.
- P코어의 전력 소비량
- E코어의 전력 소비량
- 프로세스 수
- 전체 수행 시간
- 평균 응답 시간

## ③
프로세스를 하나씩 추가해 각 프로세스의 도착 시간, 실행 시간을 설정한다. 
- 프로세스 추가와 도착 시간, 실행 시간 설정
- 무작위 프로세스 추가
- 프로세스 제거

## ④
스케줄링 결과 간트차트를 보여준다.

## ⑤
프로세스 테이블을 보여준다.
- 프로세스 테이블에는 프로세스별 AT(도착 시간), BT(실행 시간), WT(대기 시간), TT(반환 시간), NTT(실행 시간 대비 반환 시간) 정보를 볼 수 있다.

# 구현된 스케줄링 기법
아래는 구현된 스케줄링 기법의 시뮬레이션 결과이다.

## FCFS (First Come First Service)
![FCFS](https://github.com/refracta/Process-Scheduling-Simulator/assets/58779799/fefceeff-6c0c-4825-bae9-d00380ec4a65)

## RR (Round-Robin)
![RR](https://github.com/refracta/Process-Scheduling-Simulator/assets/58779799/9bc00084-aae7-4ed4-9520-88180354fdbf)

## SPN (Shortest-Process-Next)
![SPN](https://github.com/refracta/Process-Scheduling-Simulator/assets/58779799/1420af1b-ad0e-4f60-91fa-e80456d35f78)

## SRTN (Shortest-Remaining-Time-Next)
![SRTN](https://github.com/refracta/Process-Scheduling-Simulator/assets/58779799/97b22c34-3566-44de-9538-c55fdd6a82b6)

## HRRN (High-Response-Ratio-Next)
![HRRN](https://github.com/refracta/Process-Scheduling-Simulator/assets/58779799/259c793f-8760-4578-af23-5294f409edb2)

## RR2Q (Round-Robin with Run Queue, Ready Queue)
![RR2Q](https://github.com/refracta/Process-Scheduling-Simulator/assets/58779799/bea57ef2-3a14-4fe3-86f6-905cb933566e)

### 알고리즘 설명
- Preemptive scheduling
- 스케줄링 기준 : 도착 시간
- Time quantum과 Run queue의 크기를 설정해 주어야한다.
- 설명 : Round-Robin에 queue가 하나 더 추가된 구성이다. Run queue와 Ready queue를 가진다. Ready queue는 Run queue로 이동하기 전 프로세스들이 대기하는 큐이고 Run queue는 프로세서를 할당받을 수 있는  프로세스들이 있는 큐이다. Run queue는 크기가 정해져있으므로 Run queue에 있는 프로세스의 수가 Run queue의 크기보다 작을 때 Ready queue의 프로세스가 Run queue로 이동할 수 있다. Run queue에 있는 프로세스들이 Round-Robin으로 번갈아가며 Time quantum만큼 프로세서를 사용한다.

![RR2Q1](https://github.com/refracta/Process-Scheduling-Simulator/assets/58779799/26917358-656a-4d42-bba7-b7893985dbe9)

- 예시 : 만약 Run queue의 크기가 5, 프로세서를 할당 받으려는 프로세스가 13개 있다면
1. Run queue에는 프로세스가 5개, Ready queue에는 프로세스가 8개 있게 된다.
2. Run queue에 있는 프로세스는 5번 중 1번씩 Time quantum만큼 프로세서를 할당받게 된다.
3. 잔여 실행 시간이 0인 프로세스는 Run queue를 나가게 되고 나가면서 생긴 1개의 빈자리는 Ready queue에 있는 프로세스로 채운다.

### Run queue의 크기에 따른 특징
- Run queue의 크기가 1일 때 : FCFS와 같아진다.
- Run queue의 크기가 아주 클 때 : RR과 거의 같아진다.

### 목적 
실행 시간이 작은 프로세스와 큰 프로세스가 적절히 섞여서 실행을 원하는 상황일 때, Run queue에 있는 실행 시간이 작은 프로세스는 어느정도 빨리 실행 시간이 0이 되게 하고 실행 시간이 큰 프로세스는 꾸준히 실행 시간을 줄인다. 그렇게 함으로써 실행 시간이 작은 프로세스의 수는 빨리 줄이면서 실행 시간이 큰 프로세스는 실행 시간 대비 반환 시간이 너무 커지는 것을 방지한다.

## GMRL(Give More Rice cake to Loser)
![GMRL](https://github.com/refracta/Process-Scheduling-Simulator/assets/58779799/2447c111-0703-4284-94df-4b2f11599a05)

### 알고리즘 설명
- 스케줄링 기준: 도착 시간(Arrival time), 수행 시간(Burst Time)
- 사전에 Time Quantum과 Flag_Count를 설정해줄 필요성이 있음
- 설명:
Round-Robin과 SPN의 반전형을 혼합한 형태이다. 기본적으로 Round-Robin의 형태로 진행되고, 프로세스들이 CPU를 할당받을 때마다 Flag_Count가 1씩 증가한다.
만약 Flag_Count가 사전에 설정된 Flag의 상한에 도달하게 되면, 가장 실행 시간이 긴 프로세스를 골라 CPU를 Time Quantum만큼 할당한다. 그 후에는 Flag_Count를 다시 초기화시키고 Round-Robin을 반복한다.

![GMRL1](https://github.com/refracta/Process-Scheduling-Simulator/assets/58779799/d5eaf3eb-f64d-4ea2-9010-110d89039687)

- 예시: 만약 Count_Flag가 3이고 프로세스 P1, P2, P3, P4 순으로 도착했을 때 P2의 남은 실행 시간이 제일 길다면…

![GMRL2](https://github.com/refracta/Process-Scheduling-Simulator/assets/58779799/27743926-67dc-4c0c-b47c-9b15042d8edf)

남은 실행 시간이 제일 긴 P2가 Flag가 3일때 CPU를 할당받게 된다. Flag = 3인 순간 전후는 Round-Robin으로 스케줄된다.

### 특징
- 프로세스의 수가 적은 경우에는 RR과 비교하여 거의 차이점이 체감되지 않음
- Flag는 단순한 전역변수에 가까워 Overhead가 거의 발생하지 않음

### 목적
Round-Robin에서 수행 시간이 긴 프로세스들이 오랫동안 큐에서 계류하는 문제를 해결하여, 궁극적으로 프로그램의 수행 시간을 줄이고 성능을 향상시키기 위함이다.
