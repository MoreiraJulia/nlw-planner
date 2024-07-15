CREATE TABLE activities (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    occurs_at TIMESTAMP NOT NULL,
    trip_id UUID,
    FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE
)

-- O ON DELETE CASCADE significa que como essa tabela possui uma chave estrangeira que faz referência a entrada de dados em outra tabela, significa que caso a viagem seja deletada a atividade precisa ser deletada também, pois não é possivel manter uma atividade sem a viagem.