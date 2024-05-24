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
import { IDeplacementProfil } from 'app/shared/model/deplacement-profil.model';
import { getEntity, updateEntity, createEntity, reset } from './deplacement-profil.reducer';

export const DeplacementProfilUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const profils = useAppSelector(state => state.profil.entities);
  const prestations = useAppSelector(state => state.prestation.entities);
  const transports = useAppSelector(state => state.transport.entities);
  const deplacementProfilEntity = useAppSelector(state => state.deplacementProfil.entity);
  const loading = useAppSelector(state => state.deplacementProfil.loading);
  const updating = useAppSelector(state => state.deplacementProfil.updating);
  const updateSuccess = useAppSelector(state => state.deplacementProfil.updateSuccess);

  const handleClose = () => {
    navigate('/deplacement-profil' + location.search);
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
    if (values.nbDeplacement !== undefined && typeof values.nbDeplacement !== 'number') {
      values.nbDeplacement = Number(values.nbDeplacement);
    }
    if (values.kmPresta !== undefined && typeof values.kmPresta !== 'number') {
      values.kmPresta = Number(values.kmPresta);
    }

    const entity = {
      ...deplacementProfilEntity,
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
          ...deplacementProfilEntity,
          profils: deplacementProfilEntity?.profils?.map(e => e.id.toString()),
          prestations: deplacementProfilEntity?.prestations?.map(e => e.id.toString()),
          transports: deplacementProfilEntity?.transports?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="menapiApp.deplacementProfil.home.createOrEditLabel" data-cy="DeplacementProfilCreateUpdateHeading">
            <Translate contentKey="menapiApp.deplacementProfil.home.createOrEditLabel">Create or edit a DeplacementProfil</Translate>
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
                  id="deplacement-profil-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('menapiApp.deplacementProfil.nbDeplacement')}
                id="deplacement-profil-nbDeplacement"
                name="nbDeplacement"
                data-cy="nbDeplacement"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.deplacementProfil.kmPresta')}
                id="deplacement-profil-kmPresta"
                name="kmPresta"
                data-cy="kmPresta"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.deplacementProfil.profil')}
                id="deplacement-profil-profil"
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
                label={translate('menapiApp.deplacementProfil.prestation')}
                id="deplacement-profil-prestation"
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
                label={translate('menapiApp.deplacementProfil.transport')}
                id="deplacement-profil-transport"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/deplacement-profil" replace color="info">
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

export default DeplacementProfilUpdate;
