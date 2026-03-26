import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { TipoTransporte } from 'app/shared/model/enumerations/tipo-transporte.model';
import { createEntity, getEntity, updateEntity } from './transporte.reducer';

export const TransporteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const transporteEntity = useAppSelector(state => state.transporte.entity);
  const loading = useAppSelector(state => state.transporte.loading);
  const updating = useAppSelector(state => state.transporte.updating);
  const updateSuccess = useAppSelector(state => state.transporte.updateSuccess);
  const tipoTransporteValues = Object.keys(TipoTransporte);

  const handleClose = () => {
    navigate('/transporte');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }
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
    if (values.capacidadKg !== undefined && typeof values.capacidadKg !== 'number') {
      values.capacidadKg = Number(values.capacidadKg);
    }
    if (values.capacidadM3 !== undefined && typeof values.capacidadM3 !== 'number') {
      values.capacidadM3 = Number(values.capacidadM3);
    }

    const entity = {
      ...transporteEntity,
      ...values,
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
          tipoTransporte: 'ACUATICO',
          ...transporteEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="evergreenApp.transporte.home.createOrEditLabel" data-cy="TransporteCreateUpdateHeading">
            <Translate contentKey="evergreenApp.transporte.home.createOrEditLabel">Create or edit a Transporte</Translate>
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
                  id="transporte-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('evergreenApp.transporte.identificador')}
                id="transporte-identificador"
                name="identificador"
                data-cy="identificador"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.transporte.tipoTransporte')}
                id="transporte-tipoTransporte"
                name="tipoTransporte"
                data-cy="tipoTransporte"
                type="select"
              >
                {tipoTransporteValues.map(tipoTransporte => (
                  <option value={tipoTransporte} key={tipoTransporte}>
                    {translate(`evergreenApp.TipoTransporte.${tipoTransporte}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('evergreenApp.transporte.matricula')}
                id="transporte-matricula"
                name="matricula"
                data-cy="matricula"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.transporte.capacidadKg')}
                id="transporte-capacidadKg"
                name="capacidadKg"
                data-cy="capacidadKg"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.transporte.capacidadM3')}
                id="transporte-capacidadM3"
                name="capacidadM3"
                data-cy="capacidadM3"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.transporte.estado')}
                id="transporte-estado"
                name="estado"
                data-cy="estado"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('evergreenApp.transporte.fechaAsignacion')}
                id="transporte-fechaAsignacion"
                name="fechaAsignacion"
                data-cy="fechaAsignacion"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/transporte" replace color="info">
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

export default TransporteUpdate;
