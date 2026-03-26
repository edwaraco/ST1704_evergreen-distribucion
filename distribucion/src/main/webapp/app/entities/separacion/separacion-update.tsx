import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPedidos } from 'app/entities/pedido/pedido.reducer';
import { createEntity, getEntity, reset, updateEntity } from './separacion.reducer';

export const SeparacionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pedidos = useAppSelector(state => state.pedido.entities);
  const separacionEntity = useAppSelector(state => state.separacion.entity);
  const loading = useAppSelector(state => state.separacion.loading);
  const updating = useAppSelector(state => state.separacion.updating);
  const updateSuccess = useAppSelector(state => state.separacion.updateSuccess);

  const handleClose = () => {
    navigate(`/separacion${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPedidos({}));
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
    if (values.cantidad !== undefined && typeof values.cantidad !== 'number') {
      values.cantidad = Number(values.cantidad);
    }

    const entity = {
      ...separacionEntity,
      ...values,
      pedido: pedidos.find(it => it.id.toString() === values.pedido?.toString()),
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
          ...separacionEntity,
          pedido: separacionEntity?.pedido?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="evergreenApp.separacion.home.createOrEditLabel" data-cy="SeparacionCreateUpdateHeading">
            <Translate contentKey="evergreenApp.separacion.home.createOrEditLabel">Create or edit a Separacion</Translate>
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
                  id="separacion-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('evergreenApp.separacion.identificador')}
                id="separacion-identificador"
                name="identificador"
                data-cy="identificador"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.separacion.lote')}
                id="separacion-lote"
                name="lote"
                data-cy="lote"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.separacion.cantidad')}
                id="separacion-cantidad"
                name="cantidad"
                data-cy="cantidad"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.separacion.fechaRealizacion')}
                id="separacion-fechaRealizacion"
                name="fechaRealizacion"
                data-cy="fechaRealizacion"
                type="date"
              />
              <ValidatedField
                label={translate('evergreenApp.separacion.ubicacion')}
                id="separacion-ubicacion"
                name="ubicacion"
                data-cy="ubicacion"
                type="text"
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.separacion.responsable')}
                id="separacion-responsable"
                name="responsable"
                data-cy="responsable"
                type="text"
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.separacion.observaciones')}
                id="separacion-observaciones"
                name="observaciones"
                data-cy="observaciones"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                id="separacion-pedido"
                name="pedido"
                data-cy="pedido"
                label={translate('evergreenApp.separacion.pedido')}
                type="select"
                required
              >
                <option value="" key="0" />
                {pedidos
                  ? pedidos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.identificador}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/separacion" replace color="info">
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

export default SeparacionUpdate;
