--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.21
-- Dumped by pg_dump version 14.2

-- Started on 2023-08-25 23:13:04

SET statement_timeout = 0;
SET lock_timeout = 0;
--SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2357 (class 1262 OID 16384)
-- Name: market_db; Type: DATABASE; Schema: -; Owner: -
--

--CREATE DATABASE market_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'en_US.utf8';


--\connect market_db

SET statement_timeout = 0;
SET lock_timeout = 0;
--SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

--CREATE SCHEMA public;


--
-- TOC entry 2358 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 222 (class 1255 OID 33515)
-- Name: validachavepessoa(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.validachavepessoa() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare existe integer;

begin
	existe = (select count(1) from pessoa_fisica where id = NEW.pessoa_id);
	if (existe <= 0) then
		existe = (select count(1) from pessoa_juridica where id = NEW.pessoa_id);
		if (existe <= 0) then
			raise exception 'NÃ£o foi encontrado o ID e o PK da pessoa para realizar a associaÃ§Ã£o do cadastro';
		end if;
	end if;
	RETURN new;
end;
$$;


--
-- TOC entry 235 (class 1255 OID 33522)
-- Name: validachavepessoafornecedor(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.validachavepessoafornecedor() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare existe integer;

begin
	existe = (select count(1) from pessoa_fisica where id = NEW.pessoa_fornecedor_id);
	if (existe <= 0) then
		existe = (select count(1) from pessoa_juridica where id = NEW.pessoa_fornecedor_id);
		if (existe <= 0) then
			raise exception 'NÃ£o foi encontrado o ID e o PK da pessoa para realizar a associaÃ§Ã£o do cadastro';
		end if;
	end if;
	RETURN new;
end;
$$;


SET default_tablespace = '';

--
-- TOC entry 201 (class 1259 OID 33280)
-- Name: acesso; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.acesso (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


--
-- TOC entry 202 (class 1259 OID 33285)
-- Name: avaliacao_produto; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.avaliacao_produto (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    nota integer NOT NULL,
    pessoa_id bigint NOT NULL,
    produto_id bigint NOT NULL
);


--
-- TOC entry 203 (class 1259 OID 33290)
-- Name: categoria_produto; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.categoria_produto (
    id bigint NOT NULL,
    nome_desc character varying(255) NOT NULL
);


--
-- TOC entry 204 (class 1259 OID 33295)
-- Name: conta_pagar; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.conta_pagar (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    dt_pagamento date,
    dt_vencimento date NOT NULL,
    status character varying(255) NOT NULL,
    valor_desconto numeric(38,2),
    valor_total numeric(38,2) NOT NULL,
    forma_pagamento_id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    pessoa_fornecedor_id bigint NOT NULL,
    CONSTRAINT conta_pagar_status_check CHECK (((status)::text = ANY ((ARRAY['COBRANCA'::character varying, 'VENCIDA'::character varying, 'ABERTA'::character varying, 'QUITADA'::character varying, 'RENEGOCIADA'::character varying])::text[])))
);


--
-- TOC entry 205 (class 1259 OID 33304)
-- Name: conta_receber; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.conta_receber (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    dt_pagamento date,
    dt_vencimento date NOT NULL,
    status character varying(255) NOT NULL,
    valor_desconto numeric(38,2),
    valor_total numeric(38,2) NOT NULL,
    pessoa_id bigint NOT NULL,
    CONSTRAINT conta_receber_status_check CHECK (((status)::text = ANY ((ARRAY['COBRANCA'::character varying, 'VENCIDA'::character varying, 'ABERTA'::character varying, 'QUITADA'::character varying])::text[])))
);


--
-- TOC entry 206 (class 1259 OID 33313)
-- Name: cupom_desconto; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cupom_desconto (
    id bigint NOT NULL,
    cod_desc character varying(255) NOT NULL,
    data_validade_cupom date NOT NULL,
    valor_porcent_desconto numeric(38,2),
    valor_real_desc numeric(38,2)
);


--
-- TOC entry 207 (class 1259 OID 33318)
-- Name: endereco; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.endereco (
    id bigint NOT NULL,
    bairro character varying(255) NOT NULL,
    cep character varying(255) NOT NULL,
    cidade character varying(255) NOT NULL,
    complemento character varying(255),
    numero character varying(255) NOT NULL,
    rua_logra character varying(255) NOT NULL,
    tipo_endereco character varying(255) NOT NULL,
    uf character varying(255) NOT NULL,
    pessoa_id bigint NOT NULL,
    CONSTRAINT endereco_tipo_endereco_check CHECK (((tipo_endereco)::text = ANY ((ARRAY['COBRANCA'::character varying, 'ENTREGA'::character varying])::text[])))
);


--
-- TOC entry 208 (class 1259 OID 33327)
-- Name: forma_pagamento; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.forma_pagamento (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


--
-- TOC entry 209 (class 1259 OID 33332)
-- Name: imagem_produto; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.imagem_produto (
    id bigint NOT NULL,
    imagem_miniatura text NOT NULL,
    imagem_original text NOT NULL,
    produto_id bigint NOT NULL
);


--
-- TOC entry 210 (class 1259 OID 33340)
-- Name: item_venda_loja; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.item_venda_loja (
    id bigint NOT NULL,
    quantidade double precision NOT NULL,
    produto_id bigint NOT NULL,
    venda_compra_loja_id bigint NOT NULL
);


--
-- TOC entry 211 (class 1259 OID 33345)
-- Name: marca_produto; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.marca_produto (
    id bigint NOT NULL,
    nome_desc character varying(255) NOT NULL
);


--
-- TOC entry 181 (class 1259 OID 16390)
-- Name: marca_produto_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.marca_produto_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 212 (class 1259 OID 33350)
-- Name: nota_fiscal_compra; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.nota_fiscal_compra (
    id bigint NOT NULL,
    data_compra date NOT NULL,
    descricao_obs character varying(255),
    numero_nota character varying(255) NOT NULL,
    serie_nota character varying(255) NOT NULL,
    valor_desconto numeric(38,2),
    valor_icms numeric(38,2) NOT NULL,
    valor_total numeric(38,2) NOT NULL,
    conta_pagar_id bigint NOT NULL,
    pessoa_id bigint NOT NULL
);


--
-- TOC entry 213 (class 1259 OID 33358)
-- Name: nota_fiscal_venda; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.nota_fiscal_venda (
    id bigint NOT NULL,
    numero character varying(255) NOT NULL,
    pdf text NOT NULL,
    serie character varying(255) NOT NULL,
    tipo character varying(255) NOT NULL,
    xml text NOT NULL,
    venda_compra_loja_id bigint NOT NULL
);


--
-- TOC entry 214 (class 1259 OID 33366)
-- Name: nota_item_produto; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.nota_item_produto (
    id bigint NOT NULL,
    quantidade double precision NOT NULL,
    nota_fiscal_compra_id bigint NOT NULL,
    produto_id bigint NOT NULL
);


--
-- TOC entry 215 (class 1259 OID 33371)
-- Name: pessoa_fisica; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pessoa_fisica (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    telefone character varying(255) NOT NULL,
    cpf character varying(255) NOT NULL,
    data_nascimento date
);


--
-- TOC entry 216 (class 1259 OID 33379)
-- Name: pessoa_juridica; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pessoa_juridica (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    telefone character varying(255) NOT NULL,
    categoria character varying(255),
    cnpj character varying(255) NOT NULL,
    insc_estadual character varying(255) NOT NULL,
    insc_municipal character varying(255),
    nome_fantasia character varying(255) NOT NULL,
    razao_social character varying(255) NOT NULL
);


--
-- TOC entry 217 (class 1259 OID 33387)
-- Name: produto; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.produto (
    id bigint NOT NULL,
    alertaqtd_estoque boolean,
    altura double precision NOT NULL,
    ativo boolean NOT NULL,
    descricao text NOT NULL,
    largura double precision NOT NULL,
    link_youtube character varying(255),
    nome character varying(255) NOT NULL,
    peso double precision NOT NULL,
    profundidade double precision NOT NULL,
    qtd_alerta_estoque integer,
    qtd_clique integer,
    qtd_estoque integer NOT NULL,
    tipo_unidade character varying(255) NOT NULL,
    valor_venda numeric(38,2) NOT NULL
);


--
-- TOC entry 184 (class 1259 OID 24589)
-- Name: seq_acesso; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_acesso
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 200 (class 1259 OID 32956)
-- Name: seq_avaliacao_produto; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_avaliacao_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 183 (class 1259 OID 24582)
-- Name: seq_categoria_produto; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_categoria_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 190 (class 1259 OID 32796)
-- Name: seq_conta_pagar; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_conta_pagar
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 188 (class 1259 OID 32778)
-- Name: seq_conta_receber; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_conta_receber
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 191 (class 1259 OID 32808)
-- Name: seq_cupom_desconto; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_cupom_desconto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 186 (class 1259 OID 24617)
-- Name: seq_endereco; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_endereco
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 189 (class 1259 OID 32785)
-- Name: seq_forma_pagamento; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_forma_pagamento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 193 (class 1259 OID 32825)
-- Name: seq_imagem_produto; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_imagem_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 199 (class 1259 OID 32934)
-- Name: seq_item_venda_loja; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_item_venda_loja
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 182 (class 1259 OID 16392)
-- Name: seq_marca_produto; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_marca_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 194 (class 1259 OID 32843)
-- Name: seq_nota_fiscal_compra; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_nota_fiscal_compra
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 197 (class 1259 OID 32886)
-- Name: seq_nota_fiscal_venda; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_nota_fiscal_venda
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 195 (class 1259 OID 32855)
-- Name: seq_nota_item_produto; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_nota_item_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 185 (class 1259 OID 24607)
-- Name: seq_pessoa; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_pessoa
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 192 (class 1259 OID 32818)
-- Name: seq_produto; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 196 (class 1259 OID 32876)
-- Name: seq_status_rastreio; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_status_rastreio
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 187 (class 1259 OID 24635)
-- Name: seq_usuario; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 198 (class 1259 OID 32893)
-- Name: seq_venda_compra_loja; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_venda_compra_loja
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 218 (class 1259 OID 33395)
-- Name: status_rastreio; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.status_rastreio (
    id bigint NOT NULL,
    centro_distribuicao character varying(255),
    cidade character varying(255),
    estado character varying(255),
    status character varying(255),
    venda_compra_loja_id bigint NOT NULL
);


--
-- TOC entry 219 (class 1259 OID 33403)
-- Name: usuario; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuario (
    id bigint NOT NULL,
    data_atual_senha date NOT NULL,
    login character varying(255) NOT NULL,
    senha character varying(255) NOT NULL,
    pessoa_id bigint NOT NULL
);


--
-- TOC entry 220 (class 1259 OID 33411)
-- Name: usuario_acesso; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuario_acesso (
    usuario_id bigint NOT NULL,
    acesso_id bigint NOT NULL
);


--
-- TOC entry 221 (class 1259 OID 33414)
-- Name: venda_compra_loja; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.venda_compra_loja (
    id bigint NOT NULL,
    data_entrega date NOT NULL,
    data_venda date NOT NULL,
    dias_entrega integer NOT NULL,
    valor_desconto numeric(38,2),
    valor_frete numeric(38,2) NOT NULL,
    valor_total numeric(38,2) NOT NULL,
    cupom_desconto_id bigint,
    endereco_cobranca_id bigint NOT NULL,
    endereco_entrega_id bigint NOT NULL,
    forma_pagamento_id bigint NOT NULL,
    nota_fiscal_id bigint NOT NULL,
    pessoa_id bigint NOT NULL
);


--
-- TOC entry 2331 (class 0 OID 33280)
-- Dependencies: 201
-- Data for Name: acesso; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2332 (class 0 OID 33285)
-- Dependencies: 202
-- Data for Name: avaliacao_produto; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.avaliacao_produto VALUES (1, 'testeAvaliacaoProduto', 5, 1, 1);
INSERT INTO public.avaliacao_produto VALUES (2, 'testeAvaliacaoProduto', 5, 1, 1);


--
-- TOC entry 2333 (class 0 OID 33290)
-- Dependencies: 203
-- Data for Name: categoria_produto; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2334 (class 0 OID 33295)
-- Dependencies: 204
-- Data for Name: conta_pagar; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2335 (class 0 OID 33304)
-- Dependencies: 205
-- Data for Name: conta_receber; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2336 (class 0 OID 33313)
-- Dependencies: 206
-- Data for Name: cupom_desconto; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2337 (class 0 OID 33318)
-- Dependencies: 207
-- Data for Name: endereco; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2338 (class 0 OID 33327)
-- Dependencies: 208
-- Data for Name: forma_pagamento; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2339 (class 0 OID 33332)
-- Dependencies: 209
-- Data for Name: imagem_produto; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2340 (class 0 OID 33340)
-- Dependencies: 210
-- Data for Name: item_venda_loja; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2341 (class 0 OID 33345)
-- Dependencies: 211
-- Data for Name: marca_produto; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2342 (class 0 OID 33350)
-- Dependencies: 212
-- Data for Name: nota_fiscal_compra; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2343 (class 0 OID 33358)
-- Dependencies: 213
-- Data for Name: nota_fiscal_venda; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2344 (class 0 OID 33366)
-- Dependencies: 214
-- Data for Name: nota_item_produto; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2345 (class 0 OID 33371)
-- Dependencies: 215
-- Data for Name: pessoa_fisica; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.pessoa_fisica VALUES (1, 'estevao@OOO', 'Estevao', '303666550', '06077959456', '1997-08-15');


--
-- TOC entry 2346 (class 0 OID 33379)
-- Dependencies: 216
-- Data for Name: pessoa_juridica; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2347 (class 0 OID 33387)
-- Dependencies: 217
-- Data for Name: produto; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.produto VALUES (1, true, 1, true, '', 1, '', 'teste1', 1, 1, 1, 1, 1, '', 1.00);


--
-- TOC entry 2348 (class 0 OID 33395)
-- Dependencies: 218
-- Data for Name: status_rastreio; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2349 (class 0 OID 33403)
-- Dependencies: 219
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2350 (class 0 OID 33411)
-- Dependencies: 220
-- Data for Name: usuario_acesso; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2351 (class 0 OID 33414)
-- Dependencies: 221
-- Data for Name: venda_compra_loja; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2360 (class 0 OID 0)
-- Dependencies: 181
-- Name: marca_produto_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.marca_produto_seq', 1, false);


--
-- TOC entry 2361 (class 0 OID 0)
-- Dependencies: 184
-- Name: seq_acesso; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_acesso', 1, false);


--
-- TOC entry 2362 (class 0 OID 0)
-- Dependencies: 200
-- Name: seq_avaliacao_produto; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_avaliacao_produto', 1, false);


--
-- TOC entry 2363 (class 0 OID 0)
-- Dependencies: 183
-- Name: seq_categoria_produto; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_categoria_produto', 1, false);


--
-- TOC entry 2364 (class 0 OID 0)
-- Dependencies: 190
-- Name: seq_conta_pagar; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_conta_pagar', 1, false);


--
-- TOC entry 2365 (class 0 OID 0)
-- Dependencies: 188
-- Name: seq_conta_receber; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_conta_receber', 1, false);


--
-- TOC entry 2366 (class 0 OID 0)
-- Dependencies: 191
-- Name: seq_cupom_desconto; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_cupom_desconto', 1, false);


--
-- TOC entry 2367 (class 0 OID 0)
-- Dependencies: 186
-- Name: seq_endereco; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_endereco', 1, false);


--
-- TOC entry 2368 (class 0 OID 0)
-- Dependencies: 189
-- Name: seq_forma_pagamento; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_forma_pagamento', 1, false);


--
-- TOC entry 2369 (class 0 OID 0)
-- Dependencies: 193
-- Name: seq_imagem_produto; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_imagem_produto', 1, false);


--
-- TOC entry 2370 (class 0 OID 0)
-- Dependencies: 199
-- Name: seq_item_venda_loja; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_item_venda_loja', 1, false);


--
-- TOC entry 2371 (class 0 OID 0)
-- Dependencies: 182
-- Name: seq_marca_produto; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_marca_produto', 1, false);


--
-- TOC entry 2372 (class 0 OID 0)
-- Dependencies: 194
-- Name: seq_nota_fiscal_compra; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_nota_fiscal_compra', 1, false);


--
-- TOC entry 2373 (class 0 OID 0)
-- Dependencies: 197
-- Name: seq_nota_fiscal_venda; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_nota_fiscal_venda', 1, false);


--
-- TOC entry 2374 (class 0 OID 0)
-- Dependencies: 195
-- Name: seq_nota_item_produto; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_nota_item_produto', 1, false);


--
-- TOC entry 2375 (class 0 OID 0)
-- Dependencies: 185
-- Name: seq_pessoa; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_pessoa', 1, false);


--
-- TOC entry 2376 (class 0 OID 0)
-- Dependencies: 192
-- Name: seq_produto; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_produto', 1, false);


--
-- TOC entry 2377 (class 0 OID 0)
-- Dependencies: 196
-- Name: seq_status_rastreio; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_status_rastreio', 1, false);


--
-- TOC entry 2378 (class 0 OID 0)
-- Dependencies: 187
-- Name: seq_usuario; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_usuario', 1, false);


--
-- TOC entry 2379 (class 0 OID 0)
-- Dependencies: 198
-- Name: seq_venda_compra_loja; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seq_venda_compra_loja', 1, false);


--
-- TOC entry 2117 (class 2606 OID 33284)
-- Name: acesso acesso_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.acesso
    ADD CONSTRAINT acesso_pkey PRIMARY KEY (id);


--
-- TOC entry 2119 (class 2606 OID 33289)
-- Name: avaliacao_produto avaliacao_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.avaliacao_produto
    ADD CONSTRAINT avaliacao_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2121 (class 2606 OID 33294)
-- Name: categoria_produto categoria_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categoria_produto
    ADD CONSTRAINT categoria_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2123 (class 2606 OID 33303)
-- Name: conta_pagar conta_pagar_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conta_pagar
    ADD CONSTRAINT conta_pagar_pkey PRIMARY KEY (id);


--
-- TOC entry 2125 (class 2606 OID 33312)
-- Name: conta_receber conta_receber_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conta_receber
    ADD CONSTRAINT conta_receber_pkey PRIMARY KEY (id);


--
-- TOC entry 2127 (class 2606 OID 33317)
-- Name: cupom_desconto cupom_desconto_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cupom_desconto
    ADD CONSTRAINT cupom_desconto_pkey PRIMARY KEY (id);


--
-- TOC entry 2129 (class 2606 OID 33326)
-- Name: endereco endereco_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.endereco
    ADD CONSTRAINT endereco_pkey PRIMARY KEY (id);


--
-- TOC entry 2131 (class 2606 OID 33331)
-- Name: forma_pagamento forma_pagamento_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.forma_pagamento
    ADD CONSTRAINT forma_pagamento_pkey PRIMARY KEY (id);


--
-- TOC entry 2133 (class 2606 OID 33339)
-- Name: imagem_produto imagem_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.imagem_produto
    ADD CONSTRAINT imagem_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2135 (class 2606 OID 33344)
-- Name: item_venda_loja item_venda_loja_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.item_venda_loja
    ADD CONSTRAINT item_venda_loja_pkey PRIMARY KEY (id);


--
-- TOC entry 2137 (class 2606 OID 33349)
-- Name: marca_produto marca_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.marca_produto
    ADD CONSTRAINT marca_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2139 (class 2606 OID 33357)
-- Name: nota_fiscal_compra nota_fiscal_compra_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.nota_fiscal_compra
    ADD CONSTRAINT nota_fiscal_compra_pkey PRIMARY KEY (id);


--
-- TOC entry 2141 (class 2606 OID 33365)
-- Name: nota_fiscal_venda nota_fiscal_venda_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.nota_fiscal_venda
    ADD CONSTRAINT nota_fiscal_venda_pkey PRIMARY KEY (id);


--
-- TOC entry 2145 (class 2606 OID 33370)
-- Name: nota_item_produto nota_item_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.nota_item_produto
    ADD CONSTRAINT nota_item_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2147 (class 2606 OID 33378)
-- Name: pessoa_fisica pessoa_fisica_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pessoa_fisica
    ADD CONSTRAINT pessoa_fisica_pkey PRIMARY KEY (id);


--
-- TOC entry 2149 (class 2606 OID 33386)
-- Name: pessoa_juridica pessoa_juridica_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pessoa_juridica
    ADD CONSTRAINT pessoa_juridica_pkey PRIMARY KEY (id);


--
-- TOC entry 2151 (class 2606 OID 33394)
-- Name: produto produto_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.produto
    ADD CONSTRAINT produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2153 (class 2606 OID 33402)
-- Name: status_rastreio status_rastreio_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.status_rastreio
    ADD CONSTRAINT status_rastreio_pkey PRIMARY KEY (id);


--
-- TOC entry 2143 (class 2606 OID 33420)
-- Name: nota_fiscal_venda uk_5j072ony4vy0593vx0voxe5s5; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.nota_fiscal_venda
    ADD CONSTRAINT uk_5j072ony4vy0593vx0voxe5s5 UNIQUE (venda_compra_loja_id);


--
-- TOC entry 2161 (class 2606 OID 33426)
-- Name: venda_compra_loja uk_d1nka7hyceswrif51tge7tvg9; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venda_compra_loja
    ADD CONSTRAINT uk_d1nka7hyceswrif51tge7tvg9 UNIQUE (nota_fiscal_id);


--
-- TOC entry 2157 (class 2606 OID 33424)
-- Name: usuario_acesso uk_fhwpg5wu1u5p306q8gycxn9ky; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_acesso
    ADD CONSTRAINT uk_fhwpg5wu1u5p306q8gycxn9ky UNIQUE (acesso_id);


--
-- TOC entry 2159 (class 2606 OID 33422)
-- Name: usuario_acesso unique_acesso_user; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_acesso
    ADD CONSTRAINT unique_acesso_user UNIQUE (usuario_id, acesso_id);


--
-- TOC entry 2155 (class 2606 OID 33410)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 2163 (class 2606 OID 33418)
-- Name: venda_compra_loja venda_compra_loja_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venda_compra_loja
    ADD CONSTRAINT venda_compra_loja_pkey PRIMARY KEY (id);


--
-- TOC entry 2187 (class 2620 OID 33529)
-- Name: conta_receber validachavepessoa; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoa BEFORE UPDATE ON public.conta_receber FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2189 (class 2620 OID 33531)
-- Name: endereco validachavepessoa; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoa BEFORE UPDATE ON public.endereco FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2191 (class 2620 OID 33533)
-- Name: nota_fiscal_compra validachavepessoa; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoa BEFORE UPDATE ON public.nota_fiscal_compra FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2193 (class 2620 OID 33535)
-- Name: usuario validachavepessoa; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoa BEFORE UPDATE ON public.usuario FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2195 (class 2620 OID 33537)
-- Name: venda_compra_loja validachavepessoa; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoa BEFORE UPDATE ON public.venda_compra_loja FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2188 (class 2620 OID 33530)
-- Name: conta_receber validachavepessoa2; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoa2 BEFORE INSERT ON public.conta_receber FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2190 (class 2620 OID 33532)
-- Name: endereco validachavepessoa2; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoa2 BEFORE INSERT ON public.endereco FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2192 (class 2620 OID 33534)
-- Name: nota_fiscal_compra validachavepessoa2; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoa2 BEFORE INSERT ON public.nota_fiscal_compra FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2194 (class 2620 OID 33536)
-- Name: usuario validachavepessoa2; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoa2 BEFORE INSERT ON public.usuario FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2196 (class 2620 OID 33538)
-- Name: venda_compra_loja validachavepessoa2; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoa2 BEFORE INSERT ON public.venda_compra_loja FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2181 (class 2620 OID 33516)
-- Name: avaliacao_produto validachavepessoaavaliacaoproduto; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoaavaliacaoproduto BEFORE UPDATE ON public.avaliacao_produto FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2182 (class 2620 OID 33518)
-- Name: avaliacao_produto validachavepessoaavaliacaoproduto2; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoaavaliacaoproduto2 BEFORE INSERT ON public.avaliacao_produto FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2183 (class 2620 OID 33520)
-- Name: conta_pagar validachavepessoacontapagar; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoacontapagar BEFORE UPDATE ON public.conta_pagar FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2184 (class 2620 OID 33521)
-- Name: conta_pagar validachavepessoacontapagar2; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoacontapagar2 BEFORE INSERT ON public.conta_pagar FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoa();


--
-- TOC entry 2185 (class 2620 OID 33527)
-- Name: conta_pagar validachavepessoafornecedor; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoafornecedor BEFORE UPDATE ON public.conta_pagar FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoafornecedor();


--
-- TOC entry 2186 (class 2620 OID 33528)
-- Name: conta_pagar validachavepessoafornecedor2; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER validachavepessoafornecedor2 BEFORE INSERT ON public.conta_pagar FOR EACH ROW EXECUTE PROCEDURE public.validachavepessoafornecedor();


--
-- TOC entry 2174 (class 2606 OID 33477)
-- Name: usuario_acesso acesso_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_acesso
    ADD CONSTRAINT acesso_fk FOREIGN KEY (acesso_id) REFERENCES public.acesso(id);


--
-- TOC entry 2169 (class 2606 OID 33452)
-- Name: nota_fiscal_compra conta-pagar_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.nota_fiscal_compra
    ADD CONSTRAINT "conta-pagar_fk" FOREIGN KEY (conta_pagar_id) REFERENCES public.conta_pagar(id);


--
-- TOC entry 2176 (class 2606 OID 33487)
-- Name: venda_compra_loja cupom_desconto_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venda_compra_loja
    ADD CONSTRAINT cupom_desconto_fk FOREIGN KEY (cupom_desconto_id) REFERENCES public.cupom_desconto(id);


--
-- TOC entry 2177 (class 2606 OID 33492)
-- Name: venda_compra_loja endereco_cobranca_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venda_compra_loja
    ADD CONSTRAINT endereco_cobranca_fk FOREIGN KEY (endereco_cobranca_id) REFERENCES public.endereco(id);


--
-- TOC entry 2178 (class 2606 OID 33497)
-- Name: venda_compra_loja endereco_entrega_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venda_compra_loja
    ADD CONSTRAINT endereco_entrega_fk FOREIGN KEY (endereco_entrega_id) REFERENCES public.endereco(id);


--
-- TOC entry 2165 (class 2606 OID 33432)
-- Name: conta_pagar forma_pagamento_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conta_pagar
    ADD CONSTRAINT forma_pagamento_fk FOREIGN KEY (forma_pagamento_id) REFERENCES public.forma_pagamento(id);


--
-- TOC entry 2179 (class 2606 OID 33502)
-- Name: venda_compra_loja forma_pagamento_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venda_compra_loja
    ADD CONSTRAINT forma_pagamento_fk FOREIGN KEY (forma_pagamento_id) REFERENCES public.forma_pagamento(id);


--
-- TOC entry 2171 (class 2606 OID 33462)
-- Name: nota_item_produto nota_fiscal_compra_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.nota_item_produto
    ADD CONSTRAINT nota_fiscal_compra_fk FOREIGN KEY (nota_fiscal_compra_id) REFERENCES public.nota_fiscal_compra(id);


--
-- TOC entry 2180 (class 2606 OID 33507)
-- Name: venda_compra_loja nota_fiscal_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venda_compra_loja
    ADD CONSTRAINT nota_fiscal_fk FOREIGN KEY (nota_fiscal_id) REFERENCES public.nota_fiscal_venda(id);


--
-- TOC entry 2166 (class 2606 OID 33437)
-- Name: imagem_produto pessoa_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.imagem_produto
    ADD CONSTRAINT pessoa_fk FOREIGN KEY (produto_id) REFERENCES public.produto(id);


--
-- TOC entry 2164 (class 2606 OID 33427)
-- Name: avaliacao_produto produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.avaliacao_produto
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES public.produto(id);


--
-- TOC entry 2167 (class 2606 OID 33442)
-- Name: item_venda_loja produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.item_venda_loja
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES public.produto(id);


--
-- TOC entry 2172 (class 2606 OID 33467)
-- Name: nota_item_produto produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.nota_item_produto
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES public.produto(id);


--
-- TOC entry 2175 (class 2606 OID 33482)
-- Name: usuario_acesso usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_acesso
    ADD CONSTRAINT usuario_fk FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- TOC entry 2168 (class 2606 OID 33447)
-- Name: item_venda_loja venda_compra_loja_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.item_venda_loja
    ADD CONSTRAINT venda_compra_loja_fk FOREIGN KEY (venda_compra_loja_id) REFERENCES public.venda_compra_loja(id);


--
-- TOC entry 2170 (class 2606 OID 33457)
-- Name: nota_fiscal_venda venda_compra_loja_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.nota_fiscal_venda
    ADD CONSTRAINT venda_compra_loja_fk FOREIGN KEY (venda_compra_loja_id) REFERENCES public.venda_compra_loja(id);


--
-- TOC entry 2173 (class 2606 OID 33472)
-- Name: status_rastreio venda_compra_loja_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.status_rastreio
    ADD CONSTRAINT venda_compra_loja_fk FOREIGN KEY (venda_compra_loja_id) REFERENCES public.venda_compra_loja(id);


--
-- TOC entry 2359 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM market_db;
GRANT ALL ON SCHEMA public TO market_db;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2023-08-25 23:13:04

--
-- PostgreSQL database dump complete
--

