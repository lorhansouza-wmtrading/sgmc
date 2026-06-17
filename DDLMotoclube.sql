-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema sgmc
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sgmc
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sgmc` DEFAULT CHARACTER SET utf8 ;
USE `sgmc` ;

-- -----------------------------------------------------
-- Table `sgmc`.`pais`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`pais` (
  `pais_sigla` CHAR(2) NOT NULL,
  `nome` VARCHAR(150) NOT NULL,
  `continente` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`pais_sigla`),
  UNIQUE INDEX `pais_sigla_UNIQUE` (`pais_sigla` ASC) VISIBLE,
  UNIQUE INDEX `pais_nome_UNIQUE` (`nome` ASC) VISIBLE,
  UNIQUE INDEX `pais_regiao_UNIQUE` (`continente` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`identificacao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`identificacao` (
  `id_identificacao` INT NOT NULL AUTO_INCREMENT,
  `identidade` VARCHAR(45) NOT NULL,
  `tipo` VARCHAR(7) NOT NULL,
  `emissor` VARCHAR(150) NOT NULL,
  `data_emissao` DATE NOT NULL,
  `pais_sigla` CHAR(2) NOT NULL,
  PRIMARY KEY (`id_identificacao`),
  INDEX `fk_identificacao_pais1_idx` (`pais_sigla` ASC) VISIBLE,
  UNIQUE INDEX `id_identificacao_UNIQUE` (`id_identificacao` ASC) VISIBLE,
  CONSTRAINT `fk_identificacao_pais1`
    FOREIGN KEY (`pais_sigla`)
    REFERENCES `sgmc`.`pais` (`pais_sigla`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`estado_provincia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`estado_provincia` (
  `estado_sigla` VARCHAR(2) NOT NULL,
  `nome` VARCHAR(255) NOT NULL,
  `regiao` VARCHAR(255) NOT NULL,
  `pais_sigla` CHAR(2) NOT NULL,
  PRIMARY KEY (`estado_sigla`),
  UNIQUE INDEX `uf_sigla_UNIQUE` (`estado_sigla` ASC) VISIBLE,
  UNIQUE INDEX `uf_nome_UNIQUE` (`nome` ASC) VISIBLE,
  UNIQUE INDEX `regiao_UNIQUE` (`regiao` ASC) VISIBLE,
  INDEX `fk_uf_pais1_idx` (`pais_sigla` ASC) VISIBLE,
  CONSTRAINT `fk_uf_pais1`
    FOREIGN KEY (`pais_sigla`)
    REFERENCES `sgmc`.`pais` (`pais_sigla`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`cidade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`cidade` (
  `id_cidade` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(255) NOT NULL,
  `estado_sigla` VARCHAR(2) NOT NULL,
  PRIMARY KEY (`id_cidade`),
  INDEX `fk_cidade_estado_provincia1_idx` (`estado_sigla` ASC) VISIBLE,
  CONSTRAINT `fk_cidade_estado_provincia1`
    FOREIGN KEY (`estado_sigla`)
    REFERENCES `sgmc`.`estado_provincia` (`estado_sigla`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`sede`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`sede` (
  `id_sede` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(255) NOT NULL,
  `endereco` VARCHAR(255) NOT NULL,
  `bairro` VARCHAR(150) NOT NULL,
  `codigo_postal` VARCHAR(10) NOT NULL,
  `numero` VARCHAR(6) NOT NULL,
  `ativa` BIT(1) NOT NULL DEFAULT 0 COMMENT '0 - ativa\n1 - não ativa',
  `id_cidade` INT NOT NULL,
  PRIMARY KEY (`id_sede`),
  UNIQUE INDEX `idsede_UNIQUE` (`id_sede` ASC) VISIBLE,
  INDEX `fk_sede_cidade1_idx` (`id_cidade` ASC) VISIBLE,
  CONSTRAINT `fk_sede_cidade1`
    FOREIGN KEY (`id_cidade`)
    REFERENCES `sgmc`.`cidade` (`id_cidade`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`membro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`membro` (
  `id_membro` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(200) NOT NULL,
  `apelido` VARCHAR(45) NULL,
  `sexo` CHAR(1) NOT NULL,
  `email` VARCHAR(254) NOT NULL,
  `telefone` VARCHAR(15) NOT NULL COMMENT 'O VARCHAR é de tamanho 15, pois é o padrão internacional de telefone (E.164) de no máximo 15 dígitos.',
  `data_nascimento` DATE NOT NULL,
  `estado_civil` VARCHAR(15) NOT NULL,
  `nacionalidade` VARCHAR(25) NOT NULL,
  `naturalidade` VARCHAR(100) NOT NULL,
  `tem_escudo` BIT(1) NOT NULL COMMENT '0 - escudado\n1 - não escudado',
  `data_admissao` DATE NOT NULL,
  `batizado` BIT(1) NOT NULL COMMENT '0 - batizado\n1 - não batizado',
  `ativo` BIT NOT NULL DEFAULT 0 COMMENT '0 - ativo\n1 - não ativo',
  `tamanho_camisa` VARCHAR(7) NOT NULL,
  `nome_contato_emergencia` VARCHAR(100) NOT NULL,
  `telefone_contato_emergencia` VARCHAR(15) NOT NULL,
  `id_identificacao` INT NOT NULL,
  `id_sede` INT NOT NULL,
  PRIMARY KEY (`id_membro`),
  UNIQUE INDEX `idmembro_UNIQUE` (`id_membro` ASC) VISIBLE,
  INDEX `fk_membro_identificacao1_idx` (`id_identificacao` ASC) VISIBLE,
  INDEX `fk_membro_sede1_idx` (`id_sede` ASC) VISIBLE,
  CONSTRAINT `fk_membro_identificacao1`
    FOREIGN KEY (`id_identificacao`)
    REFERENCES `sgmc`.`identificacao` (`id_identificacao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_membro_sede1`
    FOREIGN KEY (`id_sede`)
    REFERENCES `sgmc`.`sede` (`id_sede`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`cargo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`cargo` (
  `id_cargo` INT NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(100) NOT NULL,
  `descricao` VARCHAR(255) NULL,
  PRIMARY KEY (`id_cargo`),
  UNIQUE INDEX `titulo_UNIQUE` (`titulo` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`condicao_seguro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`condicao_seguro` (
  `idcondicao_seguro` INT NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(45) NULL,
  `valor` DECIMAL(10,2) NULL,
  PRIMARY KEY (`idcondicao_seguro`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`seguro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`seguro` (
  `idseguro` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(255) NOT NULL,
  `id_condicao_seguro` INT NOT NULL,
  PRIMARY KEY (`idseguro`),
  INDEX `fk_seguro_condicao_seguro1_idx` (`id_condicao_seguro` ASC) VISIBLE,
  CONSTRAINT `fk_seguro_condicao_seguro1`
    FOREIGN KEY (`id_condicao_seguro`)
    REFERENCES `sgmc`.`condicao_seguro` (`idcondicao_seguro`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`marca`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`marca` (
  `id_marca` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(60) NULL,
  PRIMARY KEY (`id_marca`),
  UNIQUE INDEX `id_marca_UNIQUE` (`id_marca` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`modelo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`modelo` (
  `id_modelo` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(70) NOT NULL,
  `cilindrada` INT NOT NULL,
  `id_marca` INT NOT NULL,
  PRIMARY KEY (`id_modelo`),
  INDEX `fk_modelo_marca1_idx` (`id_marca` ASC) VISIBLE,
  CONSTRAINT `fk_modelo_marca1`
    FOREIGN KEY (`id_marca`)
    REFERENCES `sgmc`.`marca` (`id_marca`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`moto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`moto` (
  `placa` VARCHAR(7) NOT NULL,
  `ano` INT NOT NULL,
  `seguro` BIT NOT NULL COMMENT '0 - s/ seguro\n1 - c/ seguro',
  `id_membro` INT UNSIGNED NOT NULL,
  `id_seguro` INT NOT NULL,
  `id_modelo` INT NOT NULL,
  UNIQUE INDEX `placa_UNIQUE` (`placa` ASC) VISIBLE,
  INDEX `fk_moto_membro1_idx` (`id_membro` ASC) VISIBLE,
  INDEX `fk_moto_seguro1_idx` (`id_seguro` ASC) VISIBLE,
  PRIMARY KEY (`placa`),
  INDEX `fk_moto_modelo1_idx` (`id_modelo` ASC) VISIBLE,
  CONSTRAINT `fk_moto_membro1`
    FOREIGN KEY (`id_membro`)
    REFERENCES `sgmc`.`membro` (`id_membro`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_moto_seguro1`
    FOREIGN KEY (`id_seguro`)
    REFERENCES `sgmc`.`seguro` (`idseguro`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_moto_modelo1`
    FOREIGN KEY (`id_modelo`)
    REFERENCES `sgmc`.`modelo` (`id_modelo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`local`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`local` (
  `id_local` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(255) NOT NULL,
  `endereco` VARCHAR(255) NOT NULL,
  `bairro` VARCHAR(150) NOT NULL,
  `numero` VARCHAR(6) NOT NULL,
  `codigo_postal` VARCHAR(10) NULL,
  `capacidade` INT NOT NULL,
  `contato` VARCHAR(15) NULL,
  `id_cidade` INT NOT NULL,
  PRIMARY KEY (`id_local`),
  INDEX `fk_local_cidade1_idx` (`id_cidade` ASC) VISIBLE,
  CONSTRAINT `fk_local_cidade1`
    FOREIGN KEY (`id_cidade`)
    REFERENCES `sgmc`.`cidade` (`id_cidade`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`evento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`evento` (
  `id_evento` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(150) NOT NULL,
  `descricao` VARCHAR(255) NULL,
  `data_hora_inicio` DATETIME NOT NULL,
  `data_hora_fim` DATETIME NOT NULL,
  `valor` DECIMAL(10,2) NULL,
  `id_local` INT NOT NULL,
  PRIMARY KEY (`id_evento`),
  INDEX `fk_evento_local1_idx` (`id_local` ASC) VISIBLE,
  CONSTRAINT `fk_evento_local1`
    FOREIGN KEY (`id_local`)
    REFERENCES `sgmc`.`local` (`id_local`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`ficha_medica`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`ficha_medica` (
  `id_ficha_medica` INT NOT NULL AUTO_INCREMENT,
  `nome_plano` VARCHAR(100) NULL,
  `carteira_saude` VARCHAR(45) NULL,
  `tipo_sanguineo` CHAR(3) NULL,
  `alergias` VARCHAR(255) NULL,
  `medicamentos_continuos` VARCHAR(255) NULL,
  `condicoes_medicas` VARCHAR(255) NULL,
  `observacoes` VARCHAR(255) NULL,
  `id_membro` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_ficha_medica`),
  INDEX `fk_ficha_medica_membro1_idx` (`id_membro` ASC) VISIBLE,
  CONSTRAINT `fk_ficha_medica_membro1`
    FOREIGN KEY (`id_membro`)
    REFERENCES `sgmc`.`membro` (`id_membro`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`inscricao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`inscricao` (
  `id_evento` INT NOT NULL,
  `id_membro` INT UNSIGNED NOT NULL,
  `data_inscricao` DATETIME NOT NULL,
  `moto_placa` VARCHAR(7) NULL,
  PRIMARY KEY (`id_evento`, `id_membro`),
  INDEX `fk_evento_has_membro_membro1_idx` (`id_membro` ASC) VISIBLE,
  INDEX `fk_evento_has_membro_evento1_idx` (`id_evento` ASC) VISIBLE,
  INDEX `fk_participacao_moto1_idx` (`moto_placa` ASC) VISIBLE,
  CONSTRAINT `fk_evento_has_membro_evento1`
    FOREIGN KEY (`id_evento`)
    REFERENCES `sgmc`.`evento` (`id_evento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_evento_has_membro_membro1`
    FOREIGN KEY (`id_membro`)
    REFERENCES `sgmc`.`membro` (`id_membro`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_participacao_moto1`
    FOREIGN KEY (`moto_placa`)
    REFERENCES `sgmc`.`moto` (`placa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sgmc`.`posse`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgmc`.`posse` (
  `id_membro` INT UNSIGNED NOT NULL,
  `id_cargo` INT NOT NULL,
  `data_inicio` DATE NULL,
  `data_fim` DATE NULL,
  PRIMARY KEY (`id_membro`, `id_cargo`),
  INDEX `fk_membro_has_cargo_cargo1_idx` (`id_cargo` ASC) VISIBLE,
  INDEX `fk_membro_has_cargo_membro1_idx` (`id_membro` ASC) VISIBLE,
  CONSTRAINT `fk_membro_has_cargo_membro1`
    FOREIGN KEY (`id_membro`)
    REFERENCES `sgmc`.`membro` (`id_membro`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_membro_has_cargo_cargo1`
    FOREIGN KEY (`id_cargo`)
    REFERENCES `sgmc`.`cargo` (`id_cargo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
