import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProfil } from 'app/shared/model/profil.model';
import { getEntities as getProfils } from 'app/entities/profil/profil.reducer';
import { IPrestation } from 'app/shared/model/prestation.model';
import { getEntities as getPrestations } from 'app/entities/prestation/prestation.reducer';
import { ITransport } from 'app/shared/model/transport.model';
import { getEntities as getTransports } from 'app/entities/transport/transport.reducer';
import { ITransportProfil } from 'app/shared/model/transport-profil.model';
import { getEntity, updateEntity, createEntity, reset } from './transport-profil.reducer';

export const TransportProfilUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const profils = useAppSelector(state => state.profil.entities);
  const prestations = useAppSelector(state => state.prestation.entities);
  const transports = useAppSelector(state => state.transport.entities);
  const transportProfilEntity = useAppSelector(state => state.transportProfil.entity);
  const loading = useAppSelector(state => state.transportProfil.loading);
  const updating = useAppSelector(state => state.transportProfil.updating);
  const updateSuccess = useAppSelector(state => state.transportProfil.updateSuccess);

  const handleClose = () => {
    navigate('/transport-profil' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProfils({}));
    dispatch(getPrestations({}));
    dispatch(getTransports({}));
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
    if (values.nbHebdo !== undefined && typeof values.nbHebdo !== 'number') {
      values.nbHebdo = Number(values.nbHebdo);
    }
    if (values.kmHebdo !== undefined && typeof values.kmHebdo !== 'number') {
      values.kmHebdo = Number(values.kmHebdo);
    }

    const entity = {
      ...transportProfilEntity,
      ...values,
      profils: mapIdList(values.profils),
      prestations: mapIdList(values.prestations),
      transports: mapIdList(values.transports),
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
          ...transportProfilEntity,
          profils: transportProfilEntity?.profils?.map(e => e.id.toString()),
          prestations: transportProfilEntity?.prestations?.map(e => e.id.toString()),
          transports: transportProfilEntity?.transports?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="menapiApp.transportProfil.home.createOrEditLabel" data-cy="TransportProfilCreateUpdateHeading">
            <Translate contentKey="menapiApp.transportProfil.home.createOrEditLabel">Create or edit a TransportProfil</Translate>
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
                  id="transport-profil-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('menapiApp.transportProfil.nbHebdo')}
                id="transport-profil-nbHebdo"
                name="nbHebdo"
                data-cy="nbHebdo"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.transportProfil.kmHebdo')}
                id="transport-profil-kmHebdo"
                name="kmHebdo"
                data-cy="kmHebdo"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.transportProfil.profil')}
                id="transport-profil-profil"
                data-cy="profil"
                type="select"
                multiple
                name="profils"
              >
                <option value="" key="0" />
                {profils
                  ? profils.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('menapiApp.transportProfil.prestation')}
                id="transport-profil-prestation"
                data-cy="prestation"
                type="select"
                multiple
                name="prestations"
              >
                <option value="" key="0" />
                {prestations
                  ? prestations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('menapiApp.transportProfil.transport')}
                id="transport-profil-transport"
                data-cy="transport"
                type="select"
                multiple
                name="transports"
              >
                <option value="" key="0" />
                {transports
                  ? transports.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/transport-profil" replace color="info">
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

export default TransportProfilUpdate;
