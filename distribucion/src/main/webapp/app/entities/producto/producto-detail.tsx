import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './producto.reducer';

export const ProductoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productoEntity = useAppSelector(state => state.producto.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productoDetailsHeading">
          <Translate contentKey="evergreenApp.producto.detail.title">Producto</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productoEntity.id}</dd>
          <dt>
            <span id="identificador">
              <Translate contentKey="evergreenApp.producto.identificador">Identificador</Translate>
            </span>
          </dt>
          <dd>{productoEntity.identificador}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="evergreenApp.producto.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{productoEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="evergreenApp.producto.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{productoEntity.descripcion}</dd>
          <dt>
            <span id="fechaElaboracion">
              <Translate contentKey="evergreenApp.producto.fechaElaboracion">Fecha Elaboracion</Translate>
            </span>
          </dt>
          <dd>
            {productoEntity.fechaElaboracion ? (
              <TextFormat value={productoEntity.fechaElaboracion} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lote">
              <Translate contentKey="evergreenApp.producto.lote">Lote</Translate>
            </span>
          </dt>
          <dd>{productoEntity.lote}</dd>
          <dt>
            <span id="cantidad">
              <Translate contentKey="evergreenApp.producto.cantidad">Cantidad</Translate>
            </span>
          </dt>
          <dd>{productoEntity.cantidad}</dd>
          <dt>
            <span id="unidadMedida">
              <Translate contentKey="evergreenApp.producto.unidadMedida">Unidad Medida</Translate>
            </span>
          </dt>
          <dd>{productoEntity.unidadMedida}</dd>
          <dt>
            <Translate contentKey="evergreenApp.producto.pedido">Pedido</Translate>
          </dt>
          <dd>
            {productoEntity.pedidos
              ? productoEntity.pedidos.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.identificador}</a>
                    {productoEntity.pedidos && i === productoEntity.pedidos.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/producto" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/producto/${productoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductoDetail;
