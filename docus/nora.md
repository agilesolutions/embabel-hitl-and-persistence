# NORA, ROSA en DUO — architectuurkader voor Java Lead Developer / Solution Architect

> **Persoonlijke administratie — DUO architectuurcontext**  
> Doel: één overzicht van de relatie tussen **NORA → ROSA → DUO → Solution Architecture → C4 → Java/Spring Boot** en de betekenis daarvan voor mijn rol als Java Lead Developer bij DUO.

---

## 1. Kernbeeld

NORA is het overheidsbrede architectuurkader. Voor de onderwijssector wordt dit nader ingevwerkt via sectorale architectuur, waaronder ROSA. DUO vertaalt deze kaders vervolgens naar de eigen organisatie-, informatie- en IT-architectuur.

Als Java Lead Developer / architect werk ik vooral aan de onderste helft van deze keten: het vertalen van architectuurprincipes en organisatorische/sectorale kaders naar concrete solution architecture, technische keuzes en implementeerbare Java/Spring Boot-oplossingen.

```text
                         Nederlandse overheid
                                │
                                ▼
                     ┌─────────────────────┐
                     │        NORA         │
                     │ Nederlandse Overheid│
                     │ Referentie Architect.│
                     └──────────┬──────────┘
                                │
                    overheidsbrede principes,
                    afspraken en standaarden
                                │
                                ▼
                     ┌─────────────────────┐
                     │       ROSA          │
                     │ Onderwijssector     │
                     │ referentiearchitect.│
                     └──────────┬──────────┘
                                │
                                ▼
                     ┌─────────────────────┐
                     │        DUO          │
                     │ organisatie- en     │
                     │ enterprise architect│
                     └──────────┬──────────┘
                                │
                         vertaling naar
                         concrete oplossing
                                │
                                ▼
                     ┌─────────────────────┐
                     │ Solution Architecture│
                     │                     │
                     │ C4 / APIs / data    │
                     │ security / IAM      │
                     │ integration / cloud │
                     └──────────┬──────────┘
                                │
                                ▼
                     ┌─────────────────────┐
                     │ Java / Spring Boot  │
                     │ microservices       │
                     │ Kubernetes / CI-CD  │
                     │ observability       │
                     └─────────────────────┘
```

---

## 2. Wat is NORA?

**NORA — Nederlandse Overheid Referentie Architectuur** — biedt een gemeenschappelijk architectuurkader voor de Nederlandse overheid.

NORA is geen voorschrift om een bepaalde programmeertaal, framework of platform te gebruiken. Het beschrijft vooral architectuurprincipes, uitgangspunten en afspraken die helpen om overheidsorganisaties en hun dienstverlening op een samenhangende manier te ontwerpen.

Belangrijk onderscheid:

```text
NORA zegt niet:

    "Gebruik Spring Boot + Kubernetes."

NORA helpt beantwoorden:

    "Aan welke overheidsbrede architectuurprincipes
     moet deze oplossing bijdragen?"
```

Voor technische architectuur betekent dit dat een technologiekeuze uiteindelijk moet kunnen worden gerelateerd aan de eisen, principes en context waarin de oplossing wordt gerealiseerd.

---

## 3. NORA en de NORA Familie

NORA staat niet op zichzelf. Binnen de **NORA Familie** bestaan architecturen die algemene overheidsprincipes vertalen naar specifieke domeinen, ketens en organisaties.

Voor DUO is de onderwijssector van bijzonder belang.

Conceptueel:

```text
NORA
 │
 ├── overheidsbrede architectuur
 │
 └── NORA Familie
       │
       ├── domeinarchitecturen
       ├── ketenarchitecturen
       └── organisatiearchitecturen
              │
              ▼
             DUO
```

Hierdoor hoeft een architectuurvraag bij DUO niet uitsluitend vanuit NORA te worden bekeken. De relevante keten is eerder:

**NORA → onderwijssector → DUO → concrete oplossing**

---

# 4. ROSA en de onderwijssector

**ROSA — Referentie Onderwijs Sector Architectuur** — is relevant omdat DUO onderdeel is van het Nederlandse onderwijsdomein.

ROSA vertaalt architectuurvraagstukken naar de onderwijssector en ondersteunt onder meer gezamenlijke afspraken rond informatie-uitwisseling en interoperabiliteit.

Daarmee ontstaat voor DUO een tweede belangrijke architectuurlaag:

```text
NORA
  │
  │ overheid
  ▼
ROSA
  │
  │ onderwijs
  ▼
DUO
  │
  │ uitvoering / dienstverlening
  ▼
Concrete DUO-oplossing
```

Voor softwarearchitectuur betekent dit dat een oplossing niet alleen technisch correct moet zijn, maar ook moet passen binnen de relevante afspraken van de organisatie en de onderwijssector.

---

# 5. De rol van DUO

DUO vertaalt de hogere architectuurkaders naar de eigen uitvoeringscontext.

Daarbij komen onder andere vragen aan de orde zoals:

- Welke dienstverlening levert DUO?
- Met welke overheids- en onderwijsorganisaties wordt samengewerkt?
- Welke gegevens worden uitgewisseld?
- Welke API- en interoperabiliteitsafspraken gelden?
- Welke security- en IAM-eisen zijn van toepassing?
- Welke architectuurprincipes en technische standaarden gelden binnen DUO?
- Welke bestaande voorzieningen moeten worden hergebruikt?
- Welke eisen gelden voor beschikbaarheid, auditability, logging en continuïteit?
- Welke cloud-, infrastructuur- en deploymentstandaarden zijn toegestaan?

De concrete technische architectuur is dus het resultaat van meerdere kaders en constraints.

---

# 6. Wat betekent dit voor mijn rol als Java Lead Developer?

Mijn rol bevindt zich op het snijvlak van:

```text
Business / publieke dienstverlening
             │
             ▼
      Enterprise Architecture
             │
             ▼
      Solution Architecture
             │
             ▼
       Technical Design
             │
             ▼
      Java / Spring Boot
```

De belangrijkste verschuiving in denken is:

> Niet alleen een goede Spring Boot-oplossing ontwerpen, maar een oplossing ontwerpen waarvan de technische keuzes herleidbaar zijn naar de architectuur- en organisatiedoelstellingen.

Daarmee is mijn verantwoordelijkheid breder dan codekwaliteit alleen.

---

# 7. Architectuur-traceability

Een bruikbare manier om architectuurwerk te structureren is een traceability-keten:

```text
NORA
 │
 ▼
ROSA / sectorale kaders
 │
 ▼
DUO architectuur / standaarden
 │
 ▼
Architectuurprincipes & constraints
 │
 ▼
Requirements
 │
 ▼
Solution Architecture
 │
 ▼
C4 System Context
 │
 ▼
C4 Container
 │
 ▼
C4 Component
 │
 ▼
Technology Decisions / ADRs
 │
 ▼
Java / Spring Boot implementation
 │
 ▼
Tests / CI-CD / Runtime
```

Elke laag beantwoordt een andere vraag.

| Laag | Centrale vraag |
|---|---|
| NORA | Welke overheidsbrede uitgangspunten zijn relevant? |
| ROSA | Wat betekent dit voor het onderwijsdomein? |
| DUO | Hoe zijn deze uitgangspunten binnen DUO vertaald? |
| Requirements | Wat moet de oplossing realiseren? |
| Solution Architecture | Hoe realiseren we dit als systeem? |
| C4 System Context | In welke omgeving en keten bevindt het systeem zich? |
| C4 Container | Welke applicaties/services/datastores zijn nodig? |
| C4 Component | Hoe is een service intern opgebouwd? |
| ADR | Waarom is een belangrijke technische keuze gemaakt? |
| Code | Hoe wordt het ontwerp daadwerkelijk geïmplementeerd? |
| Runtime | Hoe wordt de oplossing gebouwd, gedeployed, gemonitord en beheerd? |

---

# 8. C4 als brug tussen beleid en code

Het **C4-model** is hierbij geen vervanging voor NORA of DUO-architectuur.

C4 is juist de praktische brug tussen architectuur en implementatie.

```text
                 Architectuurkaders
                NORA / ROSA / DUO
                        │
                        ▼
                Solution Architecture
                        │
                        ▼
                  C4 System Context
                        │
                        ▼
                   C4 Container
                        │
                        ▼
                  C4 Component
                        │
                        ▼
                    Source Code
```

Dit maakt het mogelijk om een architectuurdiscussie op verschillende abstractieniveaus te voeren zonder direct in code te duiken.

---

# 9. Voorbeeld: een nieuwe DUO-microservice

Een technische oplossing zou bijvoorbeeld kunnen bestaan uit:

```text
                 External systems
                       │
                       ▼
                ┌─────────────┐
                │ API Gateway │
                └──────┬──────┘
                       │
                       ▼
             ┌────────────────────┐
             │ Spring Boot service│
             │                    │
             │ REST / domain logic│
             │ security           │
             │ validation         │
             └───────┬────────────┘
                     │
             ┌───────┴────────┐
             ▼                ▼
       PostgreSQL          Kafka/event
             │                │
             └───────┬────────┘
                     ▼
                Observability
              OpenTelemetry /
                  metrics /
                logs / traces
```

Maar de architectuurvraag begint niet met:

> "Welke Spring Boot dependencies moeten we gebruiken?"

De volgorde is eerder:

1. Wat is de business-/dienstverleningscontext?
2. Welke NORA-principes zijn relevant?
3. Welke onderwijs-/ROSA-afspraken zijn relevant?
4. Welke DUO-architectuurregels gelden?
5. Welke functionele en niet-functionele requirements volgen daaruit?
6. Welke integraties en gegevensstromen zijn nodig?
7. Welke security- en IAM-eisen gelden?
8. Welke architectuurvorm past hierbij?
9. Welke technische keuzes ondersteunen deze architectuur?
10. Hoe wordt dit vertaald naar Spring Boot en de runtime?

---

# 10. Technologiekeuzes zijn afgeleide beslissingen

Technologieën zoals:

- Java
- Spring Boot
- Spring Security
- PostgreSQL
- Kafka
- Kubernetes
- Docker
- Helm
- GitOps
- Terraform
- OpenTelemetry
- Prometheus / Mimir
- Loki
- Tempo
- Grafana
- Keycloak / OIDC

zijn **implementatie- en platformkeuzes**.

Ze moeten niet automatisch worden beschouwd als architectuurprincipes.

Een ADR kan bijvoorbeeld vastleggen:

```text
Architectuurvraag
        │
        ▼
Requirement / constraint
        │
        ▼
Alternatieven
        │
        ▼
Decision
        │
        ▼
Consequences
        │
        ▼
Technology implementation
```

Dit maakt technische keuzes uitlegbaar en herleidbaar.

---

# 11. Architectuurdocumentatie die hierbij past

Voor mijn werk als Java Lead Developer / architect is een compacte documentatieset nuttig:

```text
/docs
 ├── architecture/
 │    ├── context.md
 │    ├── containers.md
 │    ├── components.md
 │    └── decisions/
 │         ├── ADR-001-....md
 │         └── ADR-002-....md
 │
 ├── api/
 ├── security/
 └── operations/
```

Waarbij:

### C4

Gebruik C4 voor:

- System Context
- Container
- Component
- eventueel Code

Gebruik Mermaid waar dat binnen de projectstandaarden past.

### ADR

Gebruik Architecture Decision Records voor beslissingen zoals:

- REST versus event-driven integration
- synchronisatie versus asynchronous processing
- databasekeuze
- messagingplatform
- authentication/authorization
- deploymentmodel
- observability
- externe versus interne service
- cloud/platformkeuze

### README

Gebruik de README voor het snel begrijpen van:

- doel
- context
- architectuur
- dependencies
- lokale ontwikkeling
- deployment
- belangrijke ontwerpkeuzes

---

# 12. Relatie met mijn Java/Spring Boot expertise

Mijn technische achtergrond kan hierdoor worden ingezet op meerdere niveaus:

```text
                    Architecture
                         ▲
                         │
                  Solution Design
                         ▲
                         │
                  C4 / ADR / APIs
                         ▲
                         │
              Spring Boot architecture
                         ▲
                         │
             Java implementation
                         ▲
                         │
            Tests / CI-CD / Kubernetes
```

De toegevoegde waarde van een lead developer/architect ligt dus niet alleen in het schrijven van code.

Een belangrijk deel is het **verbinden van architectuurintentie met uitvoerbare software**.

---

# 13. Code reviews in deze context

Code review hoeft niet hetzelfde te zijn als architectuurreview.

Een nuttige scheiding is:

```text
PR
 │
 ├── PR sanity
 │     ├── build
 │     ├── tests
 │     ├── obvious defects
 │     └── conventions
 │
 ├── specialist reviews
 │     ├── Spring Boot
 │     ├── Security
 │     ├── Persistence
 │     ├── Observability
 │     └── Unit tests
 │
 └── Architecture review
       ├── C4 consistency
       ├── ADR compliance
       ├── API boundaries
       ├── dependencies
       └── DUO architecture constraints
```

Dit sluit goed aan bij het doel om routineuze reviewbelasting te verminderen en meer tijd beschikbaar te houden voor:

- solution architecture
- technische richting
- architectuurkeuzes
- teambegeleiding
- introductie van nieuwe technologie
- complexe ontwerpvraagstukken

---

# 14. Copilot / AI als architectuurassistent

Deze architectuurketen kan ook worden gebruikt als basis voor GitHub Copilot agents en skills.

Conceptueel:

```text
                    Architecture Context
                            │
                            ▼
                 ┌─────────────────────┐
                 │ Solution Architect  │
                 │ Agent               │
                 └──────────┬──────────┘
                            │
                            ▼
                    C4 + ADR output
                            │
                            ▼
                 ┌─────────────────────┐
                 │ Coding / Specialist │
                 │ Agents              │
                 └──────────┬──────────┘
                            │
                            ▼
                     Source changes
                            │
                            ▼
                 ┌─────────────────────┐
                 │ Review Orchestrator │
                 └──────────┬──────────┘
                            │
             ┌──────────────┼──────────────┐
             ▼              ▼              ▼
          Spring         Security       Testing
          review          review          review
             │              │              │
             └──────────────┼──────────────┘
                            ▼
                     Consolidated PR
                       sanity report
```

Een verdere volwassenheidsstap is een **architecture compliance agent** die niet zelf architectuurbeleid verzint, maar bestaande, door DUO vastgestelde architectuurdocumentatie als bron gebruikt.

---

# 15. Belangrijke scheiding: norm, interpretatie en implementatie

Bij architectuurwerk moet steeds duidelijk zijn welk type uitspraak wordt gedaan.

### Niveau A — formeel kader

Bijvoorbeeld:

```text
NORA / ROSA / DUO architecture
```

Dit zijn externe of organisatorische bronnen en kaders.

### Niveau B — architectuurinterpretatie

Bijvoorbeeld:

```text
"Voor deze oplossing betekent dit dat..."
```

Dit is de architectuurvertaling naar de specifieke context.

### Niveau C — technische beslissing

Bijvoorbeeld:

```text
"Wij kiezen Spring Boot + PostgreSQL + Kafka omdat..."
```

Dit is een ontwerpbeslissing.

### Niveau D — implementatie

Bijvoorbeeld:

```java
@RestController
class ...
```

Dit is de daadwerkelijke software.

Deze scheiding voorkomt dat een technische voorkeur ten onrechte wordt gepresenteerd als een NORA- of DUO-eis.

---

# 16. Praktische checklist voor nieuwe oplossingen

## Architectuurcontext

- [ ] Is de relevante NORA-context bekend?
- [ ] Zijn relevante sectorale architectuurafspraken geïdentificeerd?
- [ ] Zijn relevante DUO-architectuurprincipes/standaarden bekend?
- [ ] Zijn wettelijke en organisatorische constraints meegenomen?

## Solution Architecture

- [ ] System Context beschreven
- [ ] Container model beschreven
- [ ] relevante Component-modellen beschreven
- [ ] belangrijkste dataflows beschreven
- [ ] API's en integraties beschreven
- [ ] security/IAM beschreven
- [ ] availability/resilience beschreven
- [ ] observability beschreven

## Technische keuzes

- [ ] Belangrijke keuzes vastgelegd als ADR
- [ ] Alternatieven overwogen
- [ ] Consequences beschreven
- [ ] Platform-/technologiekeuzes herleidbaar naar requirements

## Implementatie

- [ ] Spring Boot structureel correct
- [ ] unit tests aanwezig
- [ ] integration tests waar relevant
- [ ] security correct toegepast
- [ ] persistence correct toegepast
- [ ] observability aanwezig
- [ ] CI/CD ingericht
- [ ] deployment/configuration reproduceerbaar

## Review

- [ ] PR sanity uitgevoerd
- [ ] specialist reviews uitgevoerd waar nodig
- [ ] architectuurimpact gecontroleerd
- [ ] afwijkingen van architectuurkaders expliciet gemaakt
- [ ] documentatie bijgewerkt

---

# 17. Mijn werkmodel bij DUO

Voor mijn dagelijkse werk kan ik de volgende eenvoudige beslisstructuur hanteren:

```text
                 Is er een architectuurvraag?
                           │
                           ▼
              ┌────────────────────────┐
              │ Wat zijn de relevante  │
              │ externe/DUO kaders?    │
              └────────────┬───────────┘
                           ▼
                 Requirements / context
                           │
                           ▼
                  Solution Architecture
                           │
                           ▼
                     C4 + ADRs
                           │
                           ▼
                   Technology choices
                           │
                           ▼
                 Java / Spring Boot
                           │
                           ▼
                Automated verification
                           │
                           ▼
                     Human review
```

Het doel is niet om iedere ontwikkelaar iedere architectuurlaag volledig zelf te laten documenteren.

Het doel is om **architectuurintentie zichtbaar te maken en de vertaalslag naar software beheersbaar te houden**.

---

# 18. Kernboodschap voor mijn rol

Mijn rol als Java Lead Developer bij DUO kan architectuurmatig worden samengevat als:

> **Van overheidsbrede en organisatiebrede architectuurkaders naar concrete, onderhoudbare en technisch uitvoerbare softwarearchitectuur.**

Daarbij vormt de volgende keten het mentale model:

```text
NORA
  ↓
ROSA / onderwijssector
  ↓
DUO
  ↓
Architectuurprincipes & requirements
  ↓
Solution Architecture
  ↓
C4
  ↓
ADRs
  ↓
Java / Spring Boot
  ↓
Tests / CI-CD / Kubernetes
  ↓
Production / Observability
```

De belangrijkste waarde van de lead developer/architect zit vervolgens in het bewaken van de **samenhang tussen deze lagen**, niet in het persoonlijk uitvoeren van iedere technische activiteit.

---

## 19. Bronnen

### NORA

Officiële bron:

https://www.noraonline.nl/

Relevante onderwerpen:

- NORA Referentiearchitectuur
- NORA Familie
- Vijflaagsmodel
- architectuurprincipes
- architectuurafspraken

### ROSA

ROSA is de referentiearchitectuur voor de Nederlandse onderwijssector:

https://rosa.wikixl.nl/

### DUO

DUO:

https://www.duo.nl/

Voor specifieke DUO-architectuurkeuzes, standaarden en interne kaders moet altijd de actuele DUO-documentatie en de binnen DUO aangewezen bron worden gebruikt.

---

## 20. Samenvatting

**NORA** geeft de overheidsbrede architectuurcontext.

**ROSA** vertaalt relevante architectuurvraagstukken naar de onderwijssector.

**DUO** vertaalt deze kaders naar de eigen organisatie- en uitvoeringscontext.

**Solution Architecture** vertaalt die context naar een concreet systeemontwerp.

**C4** maakt dat ontwerp begrijpelijk en bespreekbaar op verschillende abstractieniveaus.

**ADRs** leggen belangrijke ontwerpbeslissingen en hun rationale vast.

**Java/Spring Boot** realiseert vervolgens de technische oplossing.

Daarmee ontstaat een traceerbare keten:

> **Overheidskader → sector → organisatie → oplossing → ontwerp → technologie → code → runtime.**

Dit is het architectuurperspectief dat ik kan gebruiken om mijn rol als **Java Lead Developer bij DUO** te positioneren: technische diepgang combineren met architectuur, standaardisatie, teambegeleiding en het gericht introduceren van nieuwe technologie.
