-- Tabela Usuários
CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL CHECK (perfil IN ('ADMIN', 'OPERADOR')),
    CONSTRAINT email_formato CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- Tabela Setores Produtivos
CREATE TABLE IF NOT EXISTS setor_produtivo (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT,
    responsavel VARCHAR(100)
);

-- Tabela Atividades
CREATE TABLE IF NOT EXISTS atividade (
    id SERIAL PRIMARY KEY,
    setor_id INTEGER NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    descricao TEXT,
    data_execucao DATE NOT NULL,
    CONSTRAINT fk_atividade_setor FOREIGN KEY (setor_id)
        REFERENCES setor_produtivo(id) ON DELETE CASCADE
);

-- Tabela Registros de Produção
CREATE TABLE IF NOT EXISTS registro_producao (
    id SERIAL PRIMARY KEY,
    setor_id INTEGER NOT NULL,
    data_registro DATE NOT NULL,
    produto VARCHAR(100) NOT NULL,
    quantidade NUMERIC(10, 2) NOT NULL CHECK (quantidade > 0),
    unidade VARCHAR(20) NOT NULL,
    CONSTRAINT fk_registro_setor FOREIGN KEY (setor_id)
        REFERENCES setor_produtivo(id) ON DELETE CASCADE
);

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_atividade_setor ON atividade(setor_id);
CREATE INDEX IF NOT EXISTS idx_atividade_data ON atividade(data_execucao);
CREATE INDEX IF NOT EXISTS idx_registro_setor ON registro_producao(setor_id);
CREATE INDEX IF NOT EXISTS idx_registro_data ON registro_producao(data_registro);

-- Seed usuários
INSERT INTO usuario (nome, email, senha, perfil)
VALUES ('Administrador', 'admin@ufrn.edu.br', 'admin123', 'ADMIN'),
('Operador', 'operador@ufrn.edu.br', 'operador123', 'OPERADOR')
ON CONFLICT (email) DO NOTHING;

-- Seed setores de exemplo
INSERT INTO setor_produtivo (nome, descricao, responsavel)
VALUES
    ('Piscicultura', 'Setor responsável pela criação de peixes', 'João Silva'),
    ('Avicultura', 'Setor responsável pela criação de aves', 'Maria Santos'),
    ('Bovinos', 'Setor responsável pela criação de bovinos', 'Pedro Oliveira'),
    ('Suínos', 'Setor responsável pela criação de suínos', 'Ana Costa'),
    ('Hortaliças', 'Setor responsável pelo cultivo de hortaliças', 'Carlos Souza')
ON CONFLICT (nome) DO NOTHING;

-- Comentários nas tabelas
COMMENT ON TABLE usuario IS 'Tabela de usuários do sistema';
COMMENT ON TABLE setor_produtivo IS 'Tabela de setores produtivos da EAJ';
COMMENT ON TABLE atividade IS 'Tabela de atividades realizadas nos setores';
COMMENT ON TABLE registro_producao IS 'Tabela de registros de produção dos setores';
