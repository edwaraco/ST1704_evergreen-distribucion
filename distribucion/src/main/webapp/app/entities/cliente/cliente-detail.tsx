import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cliente.reducer';

export const ClienteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const clienteEntity = useAppSelector(state => state.cliente.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clienteDetailsHeading">
          <Translate contentKey="evergreenApp.cliente.detail.title">Cliente</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{clienteEntity.id}</dd>
          <dt>
            <span id="identificador">
              <Translate contentKey="evergreenApp.cliente.identificador">Identificador</Translate>
            </span>
          </dt>
          <dd>{clienteEntity.identificador}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="evergreenApp.cliente.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{clienteEntity.nombre}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="evergreenApp.cliente.email">Email</Translate>
            </span>
          </dt>
          <dd>{clienteEntity.email}</dd>
          <dt>
            <span id="telefono">
              <Translate contentKey="evergreenApp.cliente.telefono">Telefono</Translate>
            </span>
          </dt>
          <dd>{clienteEntity.telefono}</dd>
          <dt>
            <span id="direccion">
              <Translate contentKey="evergreenApp.cliente.direccion">Direccion</Translate>
            </span>
          </dt>
          <dd>{clienteEntity.direccion}</dd>
        </dl>
        <Button tag={Link} to="/cliente" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cliente/${clienteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClienteDetail;
