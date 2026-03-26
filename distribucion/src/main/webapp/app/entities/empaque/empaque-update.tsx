import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPedidos } from 'app/entities/pedido/pedido.reducer';
import { TipoEmpaque } from 'app/shared/model/enumerations/tipo-empaque.model';
import { TamanioEmpaque } from 'app/shared/model/enumerations/tamanio-empaque.model';
import { createEntity, getEntity, reset, updateEntity } from './empaque.reducer';

export const EmpaqueUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pedidos = useAppSelector(state => state.pedido.entities);
  const empaqueEntity = useAppSelector(state => state.empaque.entity);
  const loading = useAppSelector(state => state.empaque.loading);
  const updating = useAppSelector(state => state.empaque.updating);
  const updateSuccess = useAppSelector(state => state.empaque.updateSuccess);
  const tipoEmpaqueValues = Object.keys(TipoEmpaque);
  const tamanioEmpaqueValues = Object.keys(TamanioEmpaque);

  const handleClose = () => {
    navigate(`/empaque${location.search}`);
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
    if (values.tiempoMinutos !== undefined && typeof values.tiempoMinutos !== 'number') {
      values.tiempoMinutos = Number(values.tiempoMinutos);
    }

    const entity = {
      ...empaqueEntity,
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
          tipo: 'CAJA',
          tamanio: 'PEQUENO',
          ...empaqueEntity,
          pedido: empaqueEntity?.pedido?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="evergreenApp.empaque.home.createOrEditLabel" data-cy="EmpaqueCreateUpdateHeading">
            <Translate contentKey="evergreenApp.empaque.home.createOrEditLabel">Create or edit a Empaque</Translate>
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
                  id="empaque-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('evergreenApp.empaque.identificador')}
                id="empaque-identificador"
                name="identificador"
                data-cy="identificador"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('evergreenApp.empaque.tipo')} id="empaque-tipo" name="tipo" data-cy="tipo" type="select">
                {tipoEmpaqueValues.map(tipoEmpaque => (
                  <option value={tipoEmpaque} key={tipoEmpaque}>
                    {translate(`evergreenApp.TipoEmpaque.${tipoEmpaque}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evergreenApp.empaque.tamanio')}
                id="empaque-tamanio"
                name="tamanio"
                data-cy="tamanio"
                type="select"
              >
                {tamanioEmpaqueValues.map(tamanioEmpaque => (
                  <option value={tamanioEmpaque} key={tamanioEmpaque}>
                    {translate(`evergreenApp.TamanioEmpaque.${tamanioEmpaque}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evergreenApp.empaque.cantidad')}
                id="empaque-cantidad"
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
                label={translate('evergreenApp.empaque.tiempoMinutos')}
                id="empaque-tiempoMinutos"
                name="tiempoMinutos"
                data-cy="tiempoMinutos"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.empaque.fechaRealizacion')}
                id="empaque-fechaRealizacion"
                name="fechaRealizacion"
                data-cy="fechaRealizacion"
                type="date"
              />
              <ValidatedField
                label={translate('evergreenApp.empaque.responsable')}
                id="empaque-responsable"
                name="responsable"
                data-cy="responsable"
                type="text"
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.empaque.observaciones')}
                id="empaque-observaciones"
                name="observaciones"
                data-cy="observaciones"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                id="empaque-pedido"
                name="pedido"
                data-cy="pedido"
                label={translate('evergreenApp.empaque.pedido')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/empaque" replace color="info">
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

export default EmpaqueUpdate;
