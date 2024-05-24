import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITransportProfil } from 'app/shared/model/transport-profil.model';
import { getEntities as getTransportProfils } from 'app/entities/transport-profil/transport-profil.reducer';
import { IDeplacementProfil } from 'app/shared/model/deplacement-profil.model';
import { getEntities as getDeplacementProfils } from 'app/entities/deplacement-profil/deplacement-profil.reducer';
import { ITransport } from 'app/shared/model/transport.model';
import { TypeMoteur } from 'app/shared/model/enumerations/type-moteur.model';
import { getEntity, updateEntity, createEntity, reset } from './transport.reducer';

export const TransportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const transportProfils = useAppSelector(state => state.transportProfil.entities);
  const deplacementProfils = useAppSelector(state => state.deplacementProfil.entities);
  const transportEntity = useAppSelector(state => state.transport.entity);
  const loading = useAppSelector(state => state.transport.loading);
  const updating = useAppSelector(state => state.transport.updating);
  const updateSuccess = useAppSelector(state => state.transport.updateSuccess);
  const typeMoteurValues = Object.keys(TypeMoteur);

  const handleClose = () => {
    navigate('/transport' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTransportProfils({}));
    dispatch(getDeplacementProfils({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.feKm !== undefined && typeof values.feKm !== 'number') {
      values.feKm = Number(values.feKm);
    }

    const entity = {
      ...transportEntity,
      ...values,
      transportProfils: mapIdList(values.transportProfils),
      deplacementProfils: mapIdList(values.deplacementProfils),
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
          typeMoteur: 'THERMIQUE',
          ...transportEntity,
          transportProfils: transportEntity?.transportProfils?.map(e => e.id.toString()),
          deplacementProfils: transportEntity?.deplacementProfils?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="menapiApp.transport.home.createOrEditLabel" data-cy="TransportCreateUpdateHeading">
            <Translate contentKey="menapiApp.transport.home.createOrEditLabel">Create or edit a Transport</Translate>
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
                  id="transport-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('menapiApp.transport.categorie')}
                id="transport-categorie"
                name="categorie"
                data-cy="categorie"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('menapiApp.transport.typeMoteur')}
                id="transport-typeMoteur"
                name="typeMoteur"
                data-cy="typeMoteur"
                type="select"
              >
                {typeMoteurValues.map(typeMoteur => (
                  <option value={typeMoteur} key={typeMoteur}>
                    {translate('menapiApp.TypeMoteur.' + typeMoteur)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('menapiApp.transport.feKm')}
                id="transport-feKm"
                name="feKm"
                data-cy="feKm"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.transport.transportProfil')}
                id="transport-transportProfil"
                data-cy="transportProfil"
                type="select"
                multiple
                name="transportProfils"
              >
                <option value="" key="0" />
                {transportProfils
                  ? transportProfils.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('menapiApp.transport.deplacementProfil')}
                id="transport-deplacementProfil"
                data-cy="deplacementProfil"
                type="select"
                multiple
                name="deplacementProfils"
              >
                <option value="" key="0" />
                {deplacementProfils
                  ? deplacementProfils.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/transport" replace color="info">
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

export default TransportUpdate;
