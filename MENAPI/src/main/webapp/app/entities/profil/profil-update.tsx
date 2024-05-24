import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFonction } from 'app/shared/model/fonction.model';
import { getEntities as getFonctions } from 'app/entities/fonction/fonction.reducer';
import { ITransportProfil } from 'app/shared/model/transport-profil.model';
import { getEntities as getTransportProfils } from 'app/entities/transport-profil/transport-profil.reducer';
import { IDeplacementProfil } from 'app/shared/model/deplacement-profil.model';
import { getEntities as getDeplacementProfils } from 'app/entities/deplacement-profil/deplacement-profil.reducer';
import { IMaterielProfil } from 'app/shared/model/materiel-profil.model';
import { getEntities as getMaterielProfils } from 'app/entities/materiel-profil/materiel-profil.reducer';
import { IProfil } from 'app/shared/model/profil.model';
import { getEntity, updateEntity, createEntity, reset } from './profil.reducer';

export const ProfilUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const fonctions = useAppSelector(state => state.fonction.entities);
  const transportProfils = useAppSelector(state => state.transportProfil.entities);
  const deplacementProfils = useAppSelector(state => state.deplacementProfil.entities);
  const materielProfils = useAppSelector(state => state.materielProfil.entities);
  const profilEntity = useAppSelector(state => state.profil.entity);
  const loading = useAppSelector(state => state.profil.loading);
  const updating = useAppSelector(state => state.profil.updating);
  const updateSuccess = useAppSelector(state => state.profil.updateSuccess);

  const handleClose = () => {
    navigate('/profil' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFonctions({}));
    dispatch(getTransportProfils({}));
    dispatch(getDeplacementProfils({}));
    dispatch(getMaterielProfils({}));
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

    const entity = {
      ...profilEntity,
      ...values,
      fonction: fonctions.find(it => it.id.toString() === values.fonction?.toString()),
      transportProfils: mapIdList(values.transportProfils),
      deplacementProfils: mapIdList(values.deplacementProfils),
      materielProfils: mapIdList(values.materielProfils),
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
          ...profilEntity,
          fonction: profilEntity?.fonction?.id,
          transportProfils: profilEntity?.transportProfils?.map(e => e.id.toString()),
          deplacementProfils: profilEntity?.deplacementProfils?.map(e => e.id.toString()),
          materielProfils: profilEntity?.materielProfils?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="menapiApp.profil.home.createOrEditLabel" data-cy="ProfilCreateUpdateHeading">
            <Translate contentKey="menapiApp.profil.home.createOrEditLabel">Create or edit a Profil</Translate>
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
                  id="profil-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('menapiApp.profil.nom')}
                id="profil-nom"
                name="nom"
                data-cy="nom"
                type="text"
                validate={{
                  minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('menapiApp.profil.prenom')}
                id="profil-prenom"
                name="prenom"
                data-cy="prenom"
                type="text"
                validate={{
                  minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('menapiApp.profil.email')}
                id="profil-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="profil-fonction"
                name="fonction"
                data-cy="fonction"
                label={translate('menapiApp.profil.fonction')}
                type="select"
              >
                <option value="" key="0" />
                {fonctions
                  ? fonctions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('menapiApp.profil.transportProfil')}
                id="profil-transportProfil"
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
                label={translate('menapiApp.profil.deplacementProfil')}
                id="profil-deplacementProfil"
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
              <ValidatedField
                label={translate('menapiApp.profil.materielProfil')}
                id="profil-materielProfil"
                data-cy="materielProfil"
                type="select"
                multiple
                name="materielProfils"
              >
                <option value="" key="0" />
                {materielProfils
                  ? materielProfils.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/profil" replace color="info">
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

export default ProfilUpdate;
