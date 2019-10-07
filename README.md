# SistemasCriptoServidor

Para criar banco de dados:

create sequence usuariosID start with 0;
create table tab_usuarios (
ID_USR INTEGER NOT NULL,
NOME_USR VARCHAR2(400),
SOBRENOME_USR VARCHAR2(400),
EMAIL_USR VARCHAR2(400),
LOGIN_USR VARCHAR2(400),
SENHA_USR VARCHAR2(400),
CONSTRAINT PK_USUARIO PRIMARY KEY (ID_USR)
);
