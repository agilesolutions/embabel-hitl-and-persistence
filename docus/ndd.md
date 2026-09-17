# Nederlandse Digitale Dienst (NDD) — betekenis voor mijn rol bij DUO

> **Persoonlijke administratie / werkdocument**
> Datum: september 2026
> Context: Java Lead Developer — DUO (Dienst Uitvoering Onderwijs)

---

## 1. Doel van dit document

Dit document beschrijft wat de ontwikkeling van de **Nederlandse Digitale Dienst (NDD)** en de bredere Nederlandse digitaliseringsstrategie mogelijk betekent voor mijn rol als **Java Lead Developer bij DUO**.

Het doel is om mijn eigen rol, verantwoordelijkheden en mogelijke ontwikkelrichting vast te leggen en te verbinden met:

* de Nederlandse Digitaliseringsstrategie (NDS);
* de Nederlandse Digitale Dienst (NDD);
* NORA en overheidsarchitectuur;
* DUO-architectuur;
* Java/Spring Boot engineering;
* platform engineering;
* legacy-modernisering;
* AI-assisted software engineering;
* geautomatiseerde kwaliteitsbewaking.

Dit document is nadrukkelijk een **persoonlijke werkhypothese en richtinggevend document**, geen formele beschrijving van een DUO-functie of NDD-organisatiebesluit.

---

# 2. Nederlandse Digitale Dienst

De NDD is in 2026 opgezet als onderdeel van de versterking van de digitale slagkracht van de Nederlandse overheid.

De belangrijkste gedachte is dat overheidsorganisaties meer als één digitale overheid moeten kunnen functioneren, met meer:

* gezamenlijke architectuur;
* gemeenschappelijke standaarden;
* herbruikbare bouwstenen;
* collectieve voorzieningen;
* digitale expertise;
* implementatiekracht;
* digitale autonomie;
* vermindering van legacy;
* minder leveranciersafhankelijkheid.

De NDD betekent niet dat alle ICT-activiteiten van uitvoeringsorganisaties zoals DUO centraal worden overgenomen.

De verwachte ontwikkeling is eerder:

```text
                  Nederlandse overheid
                           │
                    NDS / NDD
                           │
             Rijksbrede architectuur
                  en standaarden
                           │
                    ┌──────┴──────┐
                    │             │
                   DUO       andere organisaties
                    │             │
              eigen dienstverlening
                    │
              eigen development teams
```

De verantwoordelijkheid voor de daadwerkelijke dienstverlening blijft bij DUO, maar technische keuzes zullen steeds meer plaatsvinden binnen een gezamenlijk overheidskader.

---

# 3. De Nederlandse Digitaliseringsstrategie

De NDD moet worden gezien binnen de bredere **Nederlandse Digitaliseringsstrategie (NDS)**.

Belangrijke thema's daarin zijn onder andere:

* digitale weerbaarheid;
* digitale autonomie;
* cloud;
* AI;
* digitale infrastructuur;
* gezamenlijke voorzieningen;
* standaardisatie;
* hergebruik;
* verminderen van legacy;
* meer samenwerking tussen overheidsorganisaties.

Een belangrijke verschuiving is:

> Van vrijwillige samenwerking naar meer gezamenlijke en bindende digitale standaarden waar dat nodig is.

Voor technische professionals betekent dit dat lokale technische keuzes steeds vaker moeten kunnen worden gerelateerd aan bredere architectuur- en engineeringprincipes.

---

# 4. Relatie met NORA

Voor architectuur binnen de publieke sector blijft **NORA — Nederlandse Overheid Referentie Architectuur** een belangrijk referentiekader.

De technische besluitvorming kan daardoor steeds meer worden gezien als een keten:

```text
NDS
 │
 ↓
NDD / Rijksbrede digitale richting
 │
 ↓
NORA / overheidsarchitectuur
 │
 ↓
DUO architectuur
 │
 ↓
Solution architecture
 │
 ↓
Reference architecture
 │
 ↓
Engineering standards
 │
 ↓
Development teams
```

Voor een Lead Developer betekent dit dat technische expertise niet alleen waardevol is bij het schrijven van software, maar ook bij het vertalen van architectuurprincipes naar concrete ontwikkelstandaarden.

---

# 5. Betekenis voor DUO

DUO blijft verantwoordelijk voor zijn eigen primaire dienstverlening en applicatielandschap.

De relevante verandering zit vooral in de manier waarop technische keuzes worden gemaakt.

Een mogelijke ontwikkeling is:

```text
Voorheen:

DUO team
   │
   └── eigen technische oplossing


Steeds meer:

Rijksbrede principes
        │
        ↓
   DUO architectuur
        │
        ↓
   Reference architecture
        │
        ↓
   Development team
        │
        ↓
   Concrete implementatie
```

Dit maakt het belangrijker dat technische oplossingen:

* reproduceerbaar zijn;
* herbruikbaar zijn;
* standaardiseerbaar zijn;
* goed gedocumenteerd zijn;
* veilig zijn;
* observeerbaar zijn;
* geautomatiseerd getest worden;
* onafhankelijk van individuele ontwikkelaars kunnen worden toegepast.

---

# 6. Mijn rol als Java Lead Developer

Mijn huidige rol combineert meerdere dimensies:

* technische richting;
* development;
* coaching;
* code reviews;
* architectuur;
* introductie van nieuwe technologie;
* ondersteuning van development teams.

De traditionele interpretatie van een Lead Developer is:

```text
Architect
   ↓
Lead Developer
   ↓
Developers
   ↓
Code reviews
```

Een meer schaalbare ontwikkeling is:

```text
                    Architectuur
                         │
                         ↓
                Engineering standards
                         │
                         ↓
              Reference architecture
                         │
              ┌──────────┴──────────┐
              ↓                     ↓
       Development teams      Platform teams
              │
              ↓
       Automated quality
              │
              ↓
        Human lead review
```

Mijn toegevoegde waarde verschuift dan gedeeltelijk van **individuele controle** naar **team-overstijgende technische enablement**.

---

# 7. Mogelijke toekomstige rol: Architecture Enablement

Een interessante ontwikkelrichting is een rol die kan worden omschreven als:

> **Technical Lead / Architecture Enablement**

De focus ligt daarbij op het mogelijk maken van goede technische beslissingen door development teams.

Belangrijke activiteiten:

### Architectuur

* solution architecture;
* C4-modellen;
* architectuurprincipes;
* Architecture Decision Records (ADR's);
* technische trade-offs;
* integratiearchitectuur;
* API-architectuur.

### Engineering

* Spring Boot standards;
* Java standards;
* testing standards;
* security standards;
* observability;
* messaging;
* persistence;
* containerisatie.

### Platform

* Kubernetes;
* CI/CD;
* GitOps;
* Terraform;
* cloud;
* secrets management;
* observability platform.

### Developer Enablement

* golden paths;
* templates;
* reference implementations;
* documentatie;
* Copilot agents;
* Copilot skills;
* geautomatiseerde quality gates.

---

# 8. Spring Boot als reference architecture

Een concrete manier om deze rol vorm te geven is het ontwikkelen van een **Spring Boot reference architecture**.

Bijvoorbeeld:

```text
DUO Spring Boot Reference Architecture
│
├── Architecture
│   ├── C4
│   └── ADR
│
├── Application
│   ├── Spring Boot
│   ├── REST
│   └── Validation
│
├── Security
│   ├── OAuth2
│   ├── OIDC
│   └── Keycloak / government identity services
│
├── Persistence
│   ├── PostgreSQL
│   ├── JPA
│   └── Flyway
│
├── Messaging
│   ├── Kafka
│   └── Avro
│
├── Testing
│   ├── JUnit 5
│   ├── Testcontainers
│   └── Integration testing
│
├── Observability
│   ├── OpenTelemetry
│   ├── Metrics
│   ├── Logs
│   └── Traces
│
├── Platform
│   ├── Kubernetes
│   ├── Docker
│   ├── Helm
│   └── GitOps
│
└── Delivery
    ├── CI/CD
    ├── Security scanning
    └── Automated quality gates
```

Het doel is niet om iedere applicatie exact hetzelfde te maken.

Het doel is:

> **Een bewezen standaardpad bieden waarmee teams snel een veilige, onderhoudbare en observeerbare Spring Boot service kunnen bouwen.**

---

# 9. Golden Paths

Een belangrijke toepassing hiervan is het creëren van **golden paths**.

Een developer zou bijvoorbeeld moeten kunnen zeggen:

> "Ik moet een nieuwe Spring Boot service maken."

En vervolgens een gestandaardiseerde basis krijgen met:

```text
Spring Boot
+ Java
+ REST API
+ Security
+ PostgreSQL
+ Flyway
+ JUnit 5
+ Testcontainers
+ OpenTelemetry
+ Kubernetes
+ CI/CD
+ GitOps
```

Hiermee wordt kennis niet langer uitsluitend opgeslagen in individuele experts.

De kennis wordt opgeslagen in:

* templates;
* repositories;
* reference implementations;
* documentation;
* automation;
* Copilot skills;
* CI/CD quality gates.

---

# 10. Geautomatiseerde code quality

Een belangrijk onderdeel van deze ontwikkeling is het verminderen van handmatige, repetitieve code reviews.

Mijn huidige ontwikkeling rond GitHub Copilot agents en skills past hierin.

Een mogelijke toekomstige workflow:

```text
Developer
    │
    ↓
Copilot Skills
    │
    ├── Spring Boot
    ├── Spring Security
    ├── Persistence
    ├── JUnit
    ├── Testcontainers
    ├── Observability
    └── Architecture
    │
    ↓
Automated PR Sanity Review
    │
    ↓
Human Lead Review
```

De automatisering controleert vooral voorspelbare zaken.

De menselijke Lead Developer concentreert zich vervolgens op:

* architectuur;
* businesscontext;
* trade-offs;
* uitzonderingen;
* security-impact;
* performance;
* onderhoudbaarheid;
* lange-termijngevolgen.

---

# 11. Mijn Copilot-agentstrategie

De huidige ontwikkeling van gespecialiseerde review agents kan worden gezien als een eerste stap richting een **AI-assisted engineering governance layer**.

Mogelijke structuur:

```text
                   Review Orchestrator
                           │
             ┌─────────────┼─────────────┐
             │             │             │
             ↓             ↓             ↓
       Spring Boot     Security       Persistence
        Reviewer       Reviewer        Reviewer
             │             │             │
             ├─────────────┼─────────────┤
             │             │             │
             ↓             ↓             ↓
          Testing     Observability   Architecture
          Reviewer       Reviewer       Reviewer
             │             │             │
             └─────────────┼─────────────┘
                           ↓
                  PR Sanity Report
                           ↓
                  Human Lead Review
```

De output kan bijvoorbeeld zijn:

```text
/reviews/<feature-branch>.md
```

Daarmee ontstaat een reproduceerbaar reviewproces.

---

# 12. Waarom dit belangrijk is voor mijn tijdsbesteding

Een van mijn verantwoordelijkheden is code review.

Maar wanneer ik structureel veel tijd besteed aan het controleren van dezelfde patronen, blijft er minder tijd over voor:

* architectuur;
* technische innovatie;
* teamontwikkeling;
* cross-team alignment;
* technology introduction;
* reference architectures;
* strategische technische keuzes.

Daarom is het doel:

> **Repetitieve kwaliteitscontrole automatiseren en menselijke review reserveren voor beslissingen die daadwerkelijk senior technische expertise vereisen.**

Dit betekent niet minder verantwoordelijkheid.

Het betekent een andere schaal van verantwoordelijkheid.

```text
Traditioneel:

1 developer
    ↓
Lead Developer
    ↓
code review


Schaalbaar:

meerdere developers
       ↓
automated engineering standards
       ↓
AI-assisted review
       ↓
Lead Developer
       ↓
architecture / complex decisions
```

---

# 13. Legacy modernization

De combinatie van NDD/NDS-doelstellingen en mijn eigen achtergrond maakt **legacy modernization** een belangrijk aandachtsgebied.

Mijn ervaring omvat zowel:

```text
COBOL / Mainframe / Legacy
          │
          ↓
      Modernisation
          │
          ↓
Java / Spring Boot
          │
          ↓
Microservices
          │
          ↓
Containers / Kubernetes
          │
          ↓
Cloud / GitOps
```

Daarmee kan technische modernisering worden benaderd als een gecontroleerde transformatie in plaats van uitsluitend als een nieuwe-developmentvraag.

Belangrijke onderwerpen:

* legacy dependency mapping;
* strangler patterns;
* API façade;
* event-driven migration;
* data migration;
* domain decomposition;
* incremental replacement;
* coexistence van legacy en moderne services;
* teststrategie;
* observability tijdens migratie.

---

# 14. Cloud en digitale autonomie

De NDS besteedt aandacht aan digitale autonomie en de vermindering van ongewenste afhankelijkheden.

Voor technische architectuur betekent dit dat kennis van **open en portable technologieën** relevant blijft.

Een mogelijke technische basis:

```text
Java
Spring Boot
Docker
Kubernetes
Terraform
GitOps
OpenTelemetry
PostgreSQL
Kafka
OIDC/OAuth2
Open standards
```

Deze technologieën kunnen worden gecombineerd met verschillende cloud- en infrastructuurplatformen.

Dat ondersteunt architectuur waarbij de applicatielaag niet onnodig volledig afhankelijk wordt van één infrastructuurleverancier.

---

# 15. Relevantie van Kubernetes en GitOps

Kubernetes + GitOps past goed binnen een model waarin platformstandaarden reproduceerbaar moeten zijn.

Bijvoorbeeld:

```text
Git
 │
 ├── Application
 ├── Helm
 ├── Kubernetes manifests
 └── Infrastructure definitions
          │
          ↓
       GitOps
          │
          ↓
     Kubernetes
          │
          ↓
     Spring Boot
```

Een dergelijke aanpak maakt platformconfiguratie:

* versieerbaar;
* reproduceerbaar;
* auditbaar;
* geautomatiseerd;
* overdraagbaar.

Dit is relevant voor zowel engineering efficiency als governance.

---

# 16. Observability als standaard

Observability moet niet achteraf aan applicaties worden toegevoegd.

Een reference architecture kan daarom standaard voorzien in:

```text
Spring Boot
     │
     ↓
OpenTelemetry
     │
     ↓
Grafana Alloy / collector
     │
 ┌───┼────────┐
 ↓   ↓        ↓
Logs Metrics Traces
 │   │        │
 ↓   ↓        ↓
Loki Mimir   Tempo
       \      /
        \    /
        Grafana
```

Hiermee wordt observability onderdeel van de engineering baseline.

---

# 17. Security by default

Hetzelfde principe geldt voor security.

Een Spring Boot reference architecture kan standaard voorzien in:

* OAuth2;
* OIDC;
* service-to-service authentication;
* token validation;
* secrets management;
* secure configuration;
* dependency scanning;
* container scanning;
* least privilege;
* audit logging.

Het doel:

> Security niet als individuele expertise bij iedere developer laten ontstaan, maar zoveel mogelijk als standaard onderdeel van het engineering platform aanbieden.

---

# 18. Mijn mogelijke ontwikkelrichting

Mijn professionele ontwikkeling kan daarmee worden gezien als een verschuiving:

```text
Java Developer
      │
      ↓
Senior Java Developer
      │
      ↓
Java Lead Developer
      │
      ↓
Technical Lead /
Architecture Enablement
      │
      ↓
Solution / Platform Architect
```

Dit is geen formele carrièreplanning, maar een mogelijke richting die logisch aansluit op mijn ervaring en de veranderende digitale context binnen de overheid.

---

# 19. Verschil tussen Lead Developer en Architectuur Enablement

| Lead Developer                | Architecture Enablement         |
| ----------------------------- | ------------------------------- |
| Code review                   | Engineering standards           |
| Team coaching                 | Team-overstijgende enablement   |
| Technical decisions           | Reference architectures         |
| Development                   | Architecture                    |
| Local team context            | Cross-team context              |
| Handmatige kwaliteitscontrole | Automated quality gates         |
| Technologie toepassen         | Technologie introduceren        |
| Problemen oplossen            | Patronen voorkomen              |
| Individuele ondersteuning     | Schaalbare developer enablement |

De twee rollen sluiten elkaar niet uit.

Architecture Enablement kan juist een natuurlijke uitbreiding zijn van een ervaren Lead Developer.

---

# 20. Concrete werkagenda

Een mogelijke praktische agenda voor mijn rol:

### A. Architecture

* C4-modeling standaardiseren;
* ADR's stimuleren;
* architecture decision templates;
* solution architecture reviews;
* cross-team technische alignment.

### B. Spring Boot

* reference architecture;
* application templates;
* security baseline;
* persistence baseline;
* observability baseline.

### C. Testing

* JUnit 5;
* Testcontainers;
* integration testing;
* contract testing;
* automatische test-quality checks.

### D. Platform

* Kubernetes;
* GitOps;
* Helm;
* Terraform;
* CI/CD.

### E. AI-assisted engineering

* GitHub Copilot agents;
* Copilot skills;
* automated PR reviews;
* architecture assistance;
* documentation generation.

### F. Modernization

* legacy assessment;
* migration patterns;
* COBOL → Java modernization;
* incremental modernization;
* strangler architecture.

---

# 21. Strategisch uitgangspunt

Mijn gewenste werkmodel kan worden samengevat als:

> **Make the right way the easy way.**

Of technisch:

```text
Architecture
     ↓
Standards
     ↓
Reference implementation
     ↓
Golden path
     ↓
Automation
     ↓
AI-assisted quality
     ↓
Developer autonomy
     ↓
Human expertise where it matters
```

Het doel is niet meer controle.

Het doel is **betere technische besluitvorming met minder repetitieve overhead**.

---

# 22. Samenvatting

De ontwikkeling van de Nederlandse Digitale Dienst en de Nederlandse Digitaliseringsstrategie wijst richting een overheid waarin:

* meer gezamenlijk wordt ontwikkeld;
* architectuur belangrijker wordt;
* standaarden minder vrijblijvend worden;
* hergebruik belangrijker wordt;
* legacy actief wordt aangepakt;
* digitale autonomie belangrijker wordt;
* cloud en platformisering verder ontwikkelen;
* AI steeds meer onderdeel wordt van software engineering.

Voor mijn rol bij DUO betekent dit een mogelijke verschuiving:

```text
                VAN

      "Ik review de code"

                  ↓

                NAAR

 "Ik zorg dat teams goede code,
  architectuur en technische
  beslissingen schaalbaar kunnen
  produceren."
```

Mijn Java/Spring Boot expertise kan daarbij worden gecombineerd met:

**architecture + platform engineering + automation + AI-assisted development + legacy modernization.**

Dat biedt een mogelijke route van **Java Lead Developer** richting **Technical Lead / Architecture Enablement / Solution Architecture**, terwijl de directe verbinding met development behouden blijft.

---

## 23. Referenties

* Nederlandse Digitaliseringsstrategie (NDS)
* Nederlandse Overheid Referentie Architectuur (NORA)
* Nederlandse Digitale Dienst (NDD)
* DUO architectuur en technische ontwikkeling
* Rijksbrede digitale standaarden en collectieve voorzieningen

Dit document moet periodiek worden bijgewerkt naarmate de NDD-organisatie, NDS-uitwerking en de concrete DUO-governance verder worden ingevuld.
