-- Core business transactions table
CREATE TABLE transactions (
                              transaction_id VARCHAR(50) PRIMARY KEY,
                              amount DECIMAL(10, 2) NOT NULL,
                              status VARCHAR(20) DEFAULT 'PENDING_REVIEW',
                              reviewed_at TIMESTAMP WITH TIME ZONE,
                              version BIGINT NOT NULL DEFAULT 0
);

-- Custom global revision tracker table for Envers
CREATE TABLE custom_revinfo (
                                rev INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                revtstmp BIGINT NOT NULL,
                                user_id VARCHAR(50),
                                client_ip VARCHAR(45)
);

-- Transaction audit mirror table matching Envers naming specifications
CREATE TABLE transactions_aud (
                                  transaction_id VARCHAR(50),
                                  rev INTEGER REFERENCES custom_revinfo(rev),
                                  revtype SMALLINT,
                                  amount DECIMAL(10, 2),
                                  status VARCHAR(20),
                                  reviewed_at TIMESTAMP WITH TIME ZONE,
                                  version BIGINT,
                                  PRIMARY KEY (transaction_id, rev)
);

-- Embabel execution state table managed by JDBC persistence layer
CREATE TABLE embabel_workflow_state (
                                        instance_id VARCHAR(50) PRIMARY KEY,
                                        workflow_id VARCHAR(50) NOT NULL,
                                        current_step VARCHAR(50) NOT NULL,
                                        context_data JSONB,
                                        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);