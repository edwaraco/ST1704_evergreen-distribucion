import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './empaque.reducer';

export const EmpaqueDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const empaqueEntity = useAppSelector(state => state.empaque.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="empaqueDetailsHeading">
          <Translate contentKey="evergreenApp.empaque.detail.title">Empaque</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{empaqueEntity.id}</dd>
          <dt>
            <span id="identificador">
              <Translate contentKey="evergreenApp.empaque.identificador">Identificador</Translate>
            </span>
          </dt>
          <dd>{empaqueEntity.identificador}</dd>
          <dt>
            <span id="tipo">
              <Translate contentKey="evergreenApp.empaque.tipo">Tipo</Translate>
            </span>
          </dt>
          <dd>{empaqueEntity.tipo}</dd>
          <dt>
            <span id="tamanio">
              <Translate contentKey="evergreenApp.empaque.tamanio">Tamanio</Translate>
            </span>
          </dt>
          <dd>{empaqueEntity.tamanio}</dd>
          <dt>
            <span id="cantidad">
              <Translate contentKey="evergreenApp.empaque.cantidad">Cantidad</Translate>
            </span>
          </dt>
          <dd>{empaqueEntity.cantidad}</dd>
          <dt>
            <span id="tiempoMinutos">
              <Translate contentKey="evergreenApp.empaque.tiempoMinutos">Tiempo Minutos</Translate>
            </span>
          </dt>
          <dd>{empaqueEntity.tiempoMinutos}</dd>
          <dt>
            <span id="fechaRealizacion">
              <Translate contentKey="evergreenApp.empaque.fechaRealizacion">Fecha Realizacion</Translate>
            </span>
          </dt>
          <dd>
            {empaqueEntity.fechaRealizacion ? (
              <TextFormat value={empaqueEntity.fechaRealizacion} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="responsable">
              <Translate contentKey="evergreenApp.empaque.responsable">Responsable</Translate>
            </span>
          </dt>
          <dd>{empaqueEntity.responsable}</dd>
          <dt>
            <span id="observaciones">
              <Translate contentKey="evergreenApp.empaque.observaciones">Observaciones</Translate>
            </span>
          </dt>
          <dd>{empaqueEntity.observaciones}</dd>
          <dt>
            <Translate contentKey="evergreenApp.empaque.pedido">Pedido</Translate>
          </dt>
          <dd>{empaqueEntity.pedido ? empaqueEntity.pedido.identificador : ''}</dd>
        </dl>
        <Button tag={Link} to="/empaque" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/empaque/${empaqueEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmpaqueDetail;
