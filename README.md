# 🌱 Plant Growth Manager

> **식물 성장 관리 데스크탑 앱** — Java Swing 기반 식물 관리 애플리케이션

---

## 📌 프로젝트 개요

식물의 물주기, 비료 일정을 관리하고 성장 과정을 기록할 수 있는 Java 데스크탑 애플리케이션입니다.  
MVC 패턴을 적용하여 설계되었습니다.

---

## 🛠 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java |
| UI | Java Swing |
| Architecture | MVC Pattern |

---

## 📁 프로젝트 구조

```
team12-plant-manager/
├── main               # 실행 클래스 (진입점)
├── view               # Swing UI 클래스
├── model              # 데이터 모델 클래스
├── controller         # 이벤트 처리 로직
│   ├── Fertilizer.java
│   ├── Waterlog.java
│   └── package controller.java
└── README.md
```

---

## ▶️ 실행 방법

Java 17 이상 환경에서 아래와 같이 실행하세요.

```bash
# 컴파일
javac -d out src/**/*.java

# 실행
java -cp out main.Main
```

또는 IDE(IntelliJ, Eclipse)에서 `main.Main`을 직접 실행하세요.

---

## 💡 주요 기능

- 식물별 물주기 및 비료 일정 관리
- 성장 기록 저장 및 조회

