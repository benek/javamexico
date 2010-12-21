--Scripts para crear la base de datos. Diagrama mental.

--
--Primero lo basico, infraestructura
--

-- Usuarios del sistema
CREATE TABLE usuario(
	uid        SERIAL PRIMARY KEY,
	uname      VARCHAR(20) NOT NULL UNIQUE,
	email      VARCHAR(80) NOT NULL UNIQUE,
	password   VARCHAR(40) NOT NULL,
	fecha_alta TIMESTAMP NOT NULL,
	nombre     VARCHAR(80) NOT NULL,
	status     INTEGER NOT NULL DEFAULT 2,
	verificado BOOLEAN NOT NULL DEFAULT false,
	reputacion INTEGER NOT NULL DEFAULT 1
);

--Ligas a otros sitios (aqui se definen los sitios)
CREATE TABLE sitio(
        sid SERIAL PRIMARY KEY,
	sitio VARCHAR(80) NOT NULL UNIQUE,
	url   VARCHAR(2000),
	descripcion VARCHAR(200),
	icono VARCHAR(200)
);

--Cuentas de un usuario en otros sitios
CREATE TABLE liga_usuario(
	uid INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	sid INTEGER NOT NULL REFERENCES sitio(sid) ON DELETE CASCADE,
	data VARCHAR(2000) NOT NULL,
	PRIMARY KEY(uid, sid)
);

--Habilidades (especialides, por ejemplo Struts, JBoss, base de datos, Spring, etc)
CREATE TABLE habilidad(
	hid SERIAL PRIMARY KEY,
	nombre VARCHAR(80) NOT NULL
);

--Las habilidades de un usuario
CREATE TABLE hab_usuario(
	uid INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	hid INTEGER NOT NULL REFERENCES habilidad(hid) ON DELETE CASCADE,
	PRIMARY KEY(uid, hid)
);

--Certificados relacionados con Java
CREATE TABLE certificado(
	cid SERIAL PRIMARY KEY,
	nombre VARCHAR(80) NOT NULL
);

--Los certificados que tiene un usuario
CREATE TABLE cert_usuario(
	uid  INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	cid  INTEGER NOT NULL REFERENCES certificado(cid) ON DELETE CASCADE,
	anio INTEGER NOT NULL,
	PRIMARY KEY(uid, cid)
);

--Escuelas (universidades, escuelas tecnicas, etc)
CREATE TABLE escuela(
	eid    SERIAL PRIMARY KEY,
	nombre VARCHAR(80) NOT NULL,
	url    VARCHAR(200)
);

--Escuelas donde ha asistido un usuario
CREATE TABLE escuela_usuario(
	uid INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	eid INTEGER NOT NULL REFERENCES escuela(eid) ON DELETE CASCADE,
	ini INTEGER,
	fin INTEGER,
	grado VARCHAR(80),
	PRIMARY KEY(uid, eid)
);

--Tags que un usuario se quiera poner
CREATE TABLE tag_usuario(
	tid   SERIAL PRIMARY KEY,
	tag   VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE tag_usuario_join(
	tid INTEGER,
	uid INTEGER,
	PRIMARY KEY(tid,uid)
);

--
--La parte de foros
--

--Temas de los foros (secciones)
CREATE TABLE tema_foro(
	tid  SERIAL PRIMARY KEY,
	tema VARCHAR(200) NOT NULL,
	descripcion VARCHAR(400),
	fecha_alta TIMESTAMP NOT NULL
);

--Un foro, con un tema, creado por un usuario
CREATE TABLE foro(
	fid SERIAL PRIMARY KEY,
	tid INTEGER NOT NULL REFERENCES tema_foro(tid) ON DELETE RESTRICT,
	uid INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	status INTEGER NOT NULL DEFAULT 0,
	fecha timestamp NOT NULL,
	titulo VARCHAR(400),
	texto varchar(4000)
);

--tags que el autor le puede poner a su foro
CREATE TABLE tag_foro(
	tid   SERIAL PRIMARY KEY,
	tag   VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE tag_foro_join(
	tid INTEGER NOT NULL REFERENCES tag_foro(tid) ON DELETE CASCADE,
	fid INTEGER NOT NULL REFERENCES foro(fid) ON DELETE CASCADE,
	PRIMARY KEY(tid,fid)
);

--comentario en un foro, hecho por un usuario
CREATE TABLE coment_foro(
	cfid SERIAL PRIMARY KEY,
	fid  INTEGER NOT NULL REFERENCES foro(fid) ON DELETE RESTRICT,
	uid  INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	rt   INTEGER REFERENCES coment_foro(cfid) ON DELETE RESTRICT, --nullify
	fecha TIMESTAMP NOT NULL,
	coment VARCHAR(2000) NOT NULL
);

--votos que los usuarios le dan a un foro
CREATE TABLE voto_foro(
	vid   SERIAL PRIMARY  KEY,
	fid   INTEGER NOT NULL REFERENCES foro(fid) ON DELETE CASCADE,
	uid   INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	fecha TIMESTAMP NOT NULL,
	up    BOOLEAN NOT NULL DEFAULT true
);

--votos que los usuarios le dan a los comentarios en un foro
CREATE TABLE voto_coment_foro(
	vid   SERIAL PRIMARY  KEY,
	cfid  INTEGER NOT NULL REFERENCES coment_foro(cfid) ON DELETE CASCADE,
	uid   INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	up    BOOLEAN NOT NULL DEFAULT true,
	fecha TIMESTAMP NOT NULL
);

--
--La parte de preguntas/respuestas
--

--Una pregunta que publica un usuario
CREATE TABLE pregunta(
	pid      SERIAL PRIMARY KEY,
	uid      INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	fecha_p  TIMESTAMP NOT NULL,
	status   INTEGER NOT NULL DEFAULT 1,
	titulo   VARCHAR(200) NOT NULL,
	pregunta VARCHAR(2000) NOT NULL,
	resp_sel INTEGER, -- REFERENCES respuesta(rid) ON DELETE nullify,
	fecha_r  TIMESTAMP
);

--Una respuesta que pone un usuario a una pregunta
CREATE TABLE respuesta(
	rid   SERIAL PRIMARY KEY,
	pid   INTEGER NOT NULL REFERENCES pregunta(pid) ON DELETE CASCADE,
	uid   INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	fecha TIMESTAMP NOT NULL,
	respuesta VARCHAR(2000) NOT NULL
);

ALTER TABLE pregunta ADD FOREIGN KEY(resp_sel) REFERENCES respuesta(rid);

--Comentarios a preguntas (no es lo mismo que respuestas)
CREATE TABLE coment_pregunta(
	cid    SERIAL PRIMARY KEY,
	pid    INTEGER NOT NULL REFERENCES pregunta(pid) ON DELETE CASCADE,
	uid    INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	fecha  TIMESTAMP NOT NULL,
	coment VARCHAR(400) NOT NULL
);

--Comentarios a respuestas
CREATE TABLE coment_respuesta(
	cid    SERIAL PRIMARY KEY,
	rid    INTEGER NOT NULL REFERENCES respuesta(rid) ON DELETE CASCADE,
	uid    INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	fecha  TIMESTAMP NOT NULL,
	coment VARCHAR(400) NOT NULL
);

--Tags que el usuario le pone a sus preguntas
CREATE TABLE tag_pregunta(
	tid   SERIAL PRIMARY KEY,
	tag   VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE tag_pregunta_join(
	tid INTEGER NOT NULL REFERENCES tag_pregunta(tid) ON DELETE CASCADE,
	pid INTEGER NOT NULL REFERENCES pregunta(pid) ON DELETE CASCADE,
	PRIMARY KEY(tid,pid)
);

--Votos que los usuarios le dan a una pregunta
CREATE TABLE voto_pregunta(
	vid   SERIAL PRIMARY  KEY,
	pid   INTEGER NOT NULL REFERENCES pregunta(pid) ON DELETE CASCADE,
	uid   INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	fecha TIMESTAMP NOT NULL,
	up    BOOLEAN NOT NULL DEFAULT true
);

--Votos que los usuarios le dan a sus respuestas
CREATE TABLE voto_respuesta(
	vid   SERIAL PRIMARY  KEY,
	rid   INTEGER NOT NULL REFERENCES respuesta(rid) ON DELETE CASCADE,
	uid   INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
	fecha TIMESTAMP NOT NULL,
	up    BOOLEAN NOT NULL DEFAULT true
);

--
--La parte de blogs de usuarios
--

--Entrada al blog de un usuario
CREATE TABLE blog_post(
  bid SERIAL PRIMARY KEY,
  uid INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
  status     INTEGER NOT NULL DEFAULT 0,
  fecha_alta TIMESTAMP NOT NULL,
  comments   BOOLEAN NOT NULL DEFAULT true,
  titulo     VARCHAR(400) NOT NULL,
  texto      VARCHAR(4000) NOT NULL --o clob en otra tabla
);

--Comentarios de los usuarios en un blog
CREATE TABLE blog_coment(
	cid   SERIAL PRIMARY KEY,
	bid   INTEGER REFERENCES blog_post(bid) ON DELETE CASCADE,
	uid   INTEGER NOT NULL REFERENCES usuario(uid)  ON DELETE CASCADE,
	rt    INTEGER REFERENCES blog_coment(cid) ON DELETE RESTRICT, --nullify
	fecha TIMESTAMP NOT NULL,
	coment VARCHAR(2000) NOT NULL
);

CREATE TABLE blog_tag(
	tid   serial primary key,
	tag   VARCHAR(80) NOT NULL UNIQUE
);
CREATE TABLE tag_blog_join(
	tid INTEGER,
	bid INTEGER,
	primary key(tid, bid)
);


CREATE TABLE voto_blog(
	vid   SERIAL PRIMARY  KEY,
	bid   INTEGER NOT NULL REFERENCES blog_post(bid)  ON DELETE CASCADE,
	uid   INTEGER NOT NULL REFERENCES usuario(uid)  ON DELETE CASCADE,
	fecha TIMESTAMP NOT NULL,
	up    BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE voto_blog_coment(
	vid   SERIAL PRIMARY  KEY,
	cid   INTEGER NOT NULL REFERENCES blog_coment(cid)  ON DELETE CASCADE,
	uid   INTEGER NOT NULL REFERENCES usuario(uid)  ON DELETE CASCADE,
	fecha TIMESTAMP NOT NULL,
	up    BOOLEAN NOT NULL DEFAULT true
);

--La parte de encuestas
CREATE TABLE encuesta(
	eid     SERIAL PRIMARY KEY,
	uid     INTEGER NOT NULL REFERENCES usuario(uid)  ON DELETE RESTRICT,
	fecha   TIMESTAMP NOT NULL,
	status  INTEGER NOT NULL DEFAULT 1,
	titulo  VARCHAR(400),
	descrip VARCHAR(400)
);

CREATE TABLE opcion_encuesta(
	opid  SERIAL PRIMARY KEY,
	eid   INTEGER NOT NULL REFERENCES encuesta(eid)  ON DELETE CASCADE,
	texto VARCHAR(400)
);

CREATE TABLE voto_encuesta(
	vid    SERIAL PRIMARY KEY,
	opid   INTEGER NOT NULL REFERENCES opcion_encuesta(opid)  ON DELETE CASCADE,
	uid    INTEGER NOT NULL REFERENCES usuario(uid)  ON DELETE CASCADE,
	fecha  TIMESTAMP NOT NULL
);

CREATE TABLE coment_encuesta(
	cid    SERIAL PRIMARY KEY,
	eid    INTEGER NOT NULL REFERENCES encuesta(eid)  ON DELETE CASCADE,
	uid    INTEGER NOT NULL REFERENCES usuario(uid)  ON DELETE CASCADE,
	fecha  TIMESTAMP NOT NULL,
	coment VARCHAR(2000) NOT NULL
);

CREATE TABLE voto_coment_encuesta(
  vid   SERIAL PRIMARY KEY,
  cid   INTEGER NOT NULL REFERENCES opcion_encuesta(opid)  ON DELETE CASCADE,
  uid   INTEGER NOT NULL REFERENCES usuario(uid)  ON DELETE CASCADE,
  fecha TIMESTAMP NOT NULL,
  up    BOOLEAN NOT NULL DEFAULT true
);

--
--La parte de bolsa de trabajo
--

--Las empresas se registran aparte y administran
--sus ofertas en otra aplicacion.
CREATE TABLE chamba_empresa(
  eid SERIAL PRIMARY KEY,
  fecha_alta timestamp NOT NULL DEFAULT localtimestamp,
  status     INTEGER NOT NULL DEFAULT 2,
  nombre     VARCHAR(80) NOT NULL,
  passwd     VARCHAR(40) NOT NULL,
  nom_contacto  VARCHAR(80),
  mail_contacto VARCHAR(80),
  telefono1 VARCHAR(10),
  telefono2 VARCHAR(10),
  url       VARCHAR(1024)
);

CREATE TABLE chamba_oferta(
  ofid SERIAL PRIMARY KEY,
  eid  INTEGER REFERENCES chamba_empresa(eid) ON DELETE CASCADE,
  status INTEGER NOT NULL DEFAULT 2,
  fecha_alta timestamp NOT NULL DEFAULT localtimestamp,
  fecha_expira timestamp NOT NULL,
  titulo VARCHAR(80) NOT NULL,
  contacto VARCHAR(80),
  mail_contacto VARCHAR(80),
  telefono1 VARCHAR(10),
  telefono2 VARCHAR(10),
  url VARCHAR(1024),
  resumen VARCHAR(200),
  descripcion VARCHAR(4000)
);

CREATE TABLE chamba_tag(
  tid SERIAL PRIMARY KEY,
  tag VARCHAR(40) NOT NULL
);

CREATE TABLE chamba_oferta_tag_join(
  tid INTEGER NOT NULL REFERENCES chamba_tag(tid) ON DELETE CASCADE,
  ofid INTEGER NOT NULL REFERENCES chamba_oferta(ofid) ON DELETE CASCADE,
  PRIMARY KEY(tid, ofid)
);

CREATE TABLE chamba_anuncio(
  adid SERIAL PRIMARY KEY,
  eid  INTEGER REFERENCES chamba_empresa(eid) ON DELETE CASCADE,
  fecha_alta timestamp NOT NULL DEFAULT localtimestamp,
  fecha_expira timestamp NOT NULL
);

CREATE TABLE chamba_aviso(
  avid SERIAL PRIMARY KEY,
  eid  INTEGER REFERENCES chamba_empresa(eid) ON DELETE CASCADE,
  fecha_alta timestamp NOT NULL DEFAULT localtimestamp,
  fecha_expira timestamp NOT NULL
);

CREATE TABLE chamba_voto_oferta(
  vid SERIAL PRIMARY KEY,
  ofid INTEGER NOT NULL REFERENCES chamba_oferta(ofid) ON DELETE CASCADE,
  uid INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
  fecha TIMESTAMP NOT NULL,
  up    BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE chamba_voto_aviso(
  vid SERIAL PRIMARY KEY,
  adid INTEGER NOT NULL REFERENCES chamba_aviso(avid) ON DELETE CASCADE,
  uid INTEGER NOT NULL REFERENCES usuario(uid) ON DELETE CASCADE,
  fecha TIMESTAMP NOT NULL,
  up    BOOLEAN NOT NULL DEFAULT true
);

insert into tema_foro(  tema, descripcion, fecha_alta ) values ( 'Java', 'Todo lo relacionado al mejor lenguaje de programacion', '1960-01-01 23:03:20' );

commit;