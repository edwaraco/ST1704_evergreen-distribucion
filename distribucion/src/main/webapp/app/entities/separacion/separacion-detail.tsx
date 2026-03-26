import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './separacion.reducer';

export const SeparacionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const separacionEntity = useAppSelector(state => state.separacion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="separacionDetailsHeading">
          <Translate contentKey="evergreenApp.separacion.detail.title">Separacion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{separacionEntity.id}</dd>
          <dt>
            <span id="identificador">
              <Translate contentKey="evergreenApp.separacion.identificador">Identificador</Translate>
            </span>
          </dt>
          <dd>{separacionEntity.identificador}</dd>
          <dt>
            <span id="lote">
              <Translate contentKey="evergreenApp.separacion.lote">Lote</Translate>
            </span>
          </dt>
          <dd>{separacionEntity.lote}</dd>
          <dt>
            <span id="cantidad">
              <Translate contentKey="evergreenApp.separacion.cantidad">Cantidad</Translate>
            </span>
          </dt>
          <dd>{separacionEntity.cantidad}</dd>
          <dt>
            <span id="fechaRealizacion">
              <Translate contentKey="evergreenApp.separacion.fechaRealizacion">Fecha Realizacion</Translate>
            </span>
          </dt>
          <dd>
            {separacionEntity.fechaRealizacion ? (
              <TextFormat value={separacionEntity.fechaRealizacion} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="ubicacion">
              <Translate contentKey="evergreenApp.separacion.ubicacion">Ubicacion</Translate>
            </span>
          </dt>
          <dd>{separacionEntity.ubicacion}</dd>
          <dt>
            <span id="responsable">
              <Translate contentKey="evergreenApp.separacion.responsable">Responsable</Translate>
            </span>
          </dt>
          <dd>{separacionEntity.responsable}</dd>
          <dt>
            <span id="observaciones">
              <Translate contentKey="evergreenApp.separacion.observaciones">Observaciones</Translate>
            </span>
          </dt>
          <dd>{separacionEntity.observaciones}</dd>
          <dt>
            <Translate contentKey="evergreenApp.separacion.pedido">Pedido</Translate>
          </dt>
          <dd>{separacionEntity.pedido ? separacionEntity.pedido.identificador : ''}</dd>
        </dl>
        <Button tag={Link} to="/separacion" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/separacion/${separacionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SeparacionDetail;
