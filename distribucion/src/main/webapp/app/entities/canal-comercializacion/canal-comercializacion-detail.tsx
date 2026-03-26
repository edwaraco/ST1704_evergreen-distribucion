import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './canal-comercializacion.reducer';

export const CanalComercializacionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const canalComercializacionEntity = useAppSelector(state => state.canalComercializacion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="canalComercializacionDetailsHeading">
          <Translate contentKey="evergreenApp.canalComercializacion.detail.title">CanalComercializacion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{canalComercializacionEntity.id}</dd>
          <dt>
            <span id="identificador">
              <Translate contentKey="evergreenApp.canalComercializacion.identificador">Identificador</Translate>
            </span>
          </dt>
          <dd>{canalComercializacionEntity.identificador}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="evergreenApp.canalComercializacion.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{canalComercializacionEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="evergreenApp.canalComercializacion.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{canalComercializacionEntity.descripcion}</dd>
          <dt>
            <span id="activo">
              <Translate contentKey="evergreenApp.canalComercializacion.activo">Activo</Translate>
            </span>
          </dt>
          <dd>{canalComercializacionEntity.activo ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/canal-comercializacion" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/canal-comercializacion/${canalComercializacionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CanalComercializacionDetail;
