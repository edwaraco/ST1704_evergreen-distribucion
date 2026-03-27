import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './transporte.reducer';

export const TransporteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const transporteEntity = useAppSelector(state => state.transporte.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transporteDetailsHeading">
          <Translate contentKey="evergreenApp.transporte.detail.title">Transporte</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{transporteEntity.id}</dd>
          <dt>
            <span id="identificador">
              <Translate contentKey="evergreenApp.transporte.identificador">Identificador</Translate>
            </span>
          </dt>
          <dd>{transporteEntity.identificador}</dd>
          <dt>
            <span id="tipoTransporte">
              <Translate contentKey="evergreenApp.transporte.tipoTransporte">Tipo Transporte</Translate>
            </span>
          </dt>
          <dd>{transporteEntity.tipoTransporte}</dd>
          <dt>
            <span id="matricula">
              <Translate contentKey="evergreenApp.transporte.matricula">Matricula</Translate>
            </span>
          </dt>
          <dd>{transporteEntity.matricula}</dd>
          <dt>
            <span id="capacidadKg">
              <Translate contentKey="evergreenApp.transporte.capacidadKg">Capacidad Kg</Translate>
            </span>
          </dt>
          <dd>{transporteEntity.capacidadKg}</dd>
          <dt>
            <span id="capacidadM3">
              <Translate contentKey="evergreenApp.transporte.capacidadM3">Capacidad M 3</Translate>
            </span>
          </dt>
          <dd>{transporteEntity.capacidadM3}</dd>
          <dt>
            <span id="estado">
              <Translate contentKey="evergreenApp.transporte.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{transporteEntity.estado}</dd>
          <dt>
            <span id="fechaAsignacion">
              <Translate contentKey="evergreenApp.transporte.fechaAsignacion">Fecha Asignacion</Translate>
            </span>
          </dt>
          <dd>
            {transporteEntity.fechaAsignacion ? (
              <TextFormat value={transporteEntity.fechaAsignacion} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/transporte" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transporte/${transporteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransporteDetail;
