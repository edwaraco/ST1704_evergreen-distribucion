import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getClientes } from 'app/entities/cliente/cliente.reducer';
import { getEntities as getProductos } from 'app/entities/producto/producto.reducer';
import { getEntities as getCanalComercializacions } from 'app/entities/canal-comercializacion/canal-comercializacion.reducer';
import { getEntities as getTransportes } from 'app/entities/transporte/transporte.reducer';
import { createEntity, getEntity, reset, updateEntity } from './pedido.reducer';

export const PedidoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clientes = useAppSelector(state => state.cliente.entities);
  const productos = useAppSelector(state => state.producto.entities);
  const canalComercializacions = useAppSelector(state => state.canalComercializacion.entities);
  const transportes = useAppSelector(state => state.transporte.entities);
  const pedidoEntity = useAppSelector(state => state.pedido.entity);
  const loading = useAppSelector(state => state.pedido.loading);
  const updating = useAppSelector(state => state.pedido.updating);
  const updateSuccess = useAppSelector(state => state.pedido.updateSuccess);

  const handleClose = () => {
    navigate(`/pedido${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClientes({}));
    dispatch(getProductos({}));
    dispatch(getCanalComercializacions({}));
    dispatch(getTransportes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...pedidoEntity,
      ...values,
      cliente: clientes.find(it => it.id.toString() === values.cliente?.toString()),
      productos: mapIdList(values.productos),
      canalComercializacion: canalComercializacions.find(it => it.id.toString() === values.canalComercializacion?.toString()),
      transporte: transportes.find(it => it.id.toString() === values.transporte?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...pedidoEntity,
          cliente: pedidoEntity?.cliente?.id,
          productos: pedidoEntity?.productos?.map(e => e.id.toString()),
          canalComercializacion: pedidoEntity?.canalComercializacion?.id,
          transporte: pedidoEntity?.transporte?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="evergreenApp.pedido.home.createOrEditLabel" data-cy="PedidoCreateUpdateHeading">
            <Translate contentKey="evergreenApp.pedido.home.createOrEditLabel">Create or edit a Pedido</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="pedido-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('evergreenApp.pedido.identificador')}
                id="pedido-identificador"
                name="identificador"
                data-cy="identificador"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.pedido.fechaEntrada')}
                id="pedido-fechaEntrada"
                name="fechaEntrada"
                data-cy="fechaEntrada"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.pedido.fechaSalida')}
                id="pedido-fechaSalida"
                name="fechaSalida"
                data-cy="fechaSalida"
                type="date"
              />
              <ValidatedField
                label={translate('evergreenApp.pedido.estado')}
                id="pedido-estado"
                name="estado"
                data-cy="estado"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                id="pedido-cliente"
                name="cliente"
                data-cy="cliente"
                label={translate('evergreenApp.pedido.cliente')}
                type="select"
                required
              >
                <option value="" key="0" />
                {clientes
                  ? clientes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                label={translate('evergreenApp.pedido.producto')}
                id="pedido-producto"
                data-cy="producto"
                type="select"
                multiple
                name="productos"
              >
                <option value="" key="0" />
                {productos
                  ? productos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="pedido-canalComercializacion"
                name="canalComercializacion"
                data-cy="canalComercializacion"
                label={translate('evergreenApp.pedido.canalComercializacion')}
                type="select"
              >
                <option value="" key="0" />
                {canalComercializacions
                  ? canalComercializacions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="pedido-transporte"
                name="transporte"
                data-cy="transporte"
                label={translate('evergreenApp.pedido.transporte')}
                type="select"
              >
                <option value="" key="0" />
                {transportes
                  ? transportes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.identificador}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pedido" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PedidoUpdate;
