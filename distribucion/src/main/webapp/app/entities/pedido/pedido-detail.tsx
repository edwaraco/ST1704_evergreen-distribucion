import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pedido.reducer';

export const PedidoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pedidoEntity = useAppSelector(state => state.pedido.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pedidoDetailsHeading">
          <Translate contentKey="evergreenApp.pedido.detail.title">Pedido</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pedidoEntity.id}</dd>
          <dt>
            <span id="identificador">
              <Translate contentKey="evergreenApp.pedido.identificador">Identificador</Translate>
            </span>
          </dt>
          <dd>{pedidoEntity.identificador}</dd>
          <dt>
            <span id="fechaEntrada">
              <Translate contentKey="evergreenApp.pedido.fechaEntrada">Fecha Entrada</Translate>
            </span>
          </dt>
          <dd>
            {pedidoEntity.fechaEntrada ? <TextFormat value={pedidoEntity.fechaEntrada} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="fechaSalida">
              <Translate contentKey="evergreenApp.pedido.fechaSalida">Fecha Salida</Translate>
            </span>
          </dt>
          <dd>
            {pedidoEntity.fechaSalida ? <TextFormat value={pedidoEntity.fechaSalida} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="estado">
              <Translate contentKey="evergreenApp.pedido.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{pedidoEntity.estado}</dd>
          <dt>
            <Translate contentKey="evergreenApp.pedido.cliente">Cliente</Translate>
          </dt>
          <dd>{pedidoEntity.cliente ? pedidoEntity.cliente.nombre : ''}</dd>
          <dt>
            <Translate contentKey="evergreenApp.pedido.producto">Producto</Translate>
          </dt>
          <dd>
            {pedidoEntity.productos
              ? pedidoEntity.productos.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.nombre}</a>
                    {pedidoEntity.productos && i === pedidoEntity.productos.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="evergreenApp.pedido.canalComercializacion">Canal Comercializacion</Translate>
          </dt>
          <dd>{pedidoEntity.canalComercializacion ? pedidoEntity.canalComercializacion.nombre : ''}</dd>
          <dt>
            <Translate contentKey="evergreenApp.pedido.transporte">Transporte</Translate>
          </dt>
          <dd>{pedidoEntity.transporte ? pedidoEntity.transporte.identificador : ''}</dd>
        </dl>
        <Button tag={Link} to="/pedido" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pedido/${pedidoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PedidoDetail;
