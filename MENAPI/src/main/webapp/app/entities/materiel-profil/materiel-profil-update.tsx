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
import { IMateriel } from 'app/shared/model/materiel.model';
import { getEntities as getMateriels } from 'app/entities/materiel/materiel.reducer';
import { IMaterielProfil } from 'app/shared/model/materiel-profil.model';
import { getEntity, updateEntity, createEntity, reset } from './materiel-profil.reducer';

export const MaterielProfilUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const profils = useAppSelector(state => state.profil.entities);
  const prestations = useAppSelector(state => state.prestation.entities);
  const materiels = useAppSelector(state => state.materiel.entities);
  const materielProfilEntity = useAppSelector(state => state.materielProfil.entity);
  const loading = useAppSelector(state => state.materielProfil.loading);
  const updating = useAppSelector(state => state.materielProfil.updating);
  const updateSuccess = useAppSelector(state => state.materielProfil.updateSuccess);

  const handleClose = () => {
    navigate('/materiel-profil' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProfils({}));
    dispatch(getPrestations({}));
    dispatch(getMateriels({}));
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
    if (values.dureeHebdo !== undefined && typeof values.dureeHebdo !== 'number') {
      values.dureeHebdo = Number(values.dureeHebdo);
    }

    const entity = {
      ...materielProfilEntity,
      ...values,
      profils: mapIdList(values.profils),
      prestations: mapIdList(values.prestations),
      materiels: mapIdList(values.materiels),
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
          ...materielProfilEntity,
          profils: materielProfilEntity?.profils?.map(e => e.id.toString()),
          prestations: materielProfilEntity?.prestations?.map(e => e.id.toString()),
          materiels: materielProfilEntity?.materiels?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="menapiApp.materielProfil.home.createOrEditLabel" data-cy="MaterielProfilCreateUpdateHeading">
            <Translate contentKey="menapiApp.materielProfil.home.createOrEditLabel">Create or edit a MaterielProfil</Translate>
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
                  id="materiel-profil-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('menapiApp.materielProfil.dureeHebdo')}
                id="materiel-profil-dureeHebdo"
                name="dureeHebdo"
                data-cy="dureeHebdo"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.materielProfil.estNeuf')}
                id="materiel-profil-estNeuf"
                name="estNeuf"
                data-cy="estNeuf"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('menapiApp.materielProfil.profil')}
                id="materiel-profil-profil"
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
                label={translate('menapiApp.materielProfil.prestation')}
                id="materiel-profil-prestation"
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
                label={translate('menapiApp.materielProfil.materiel')}
                id="materiel-profil-materiel"
                data-cy="materiel"
                type="select"
                multiple
                name="materiels"
              >
                <option value="" key="0" />
                {materiels
                  ? materiels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/materiel-profil" replace color="info">
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

export default MaterielProfilUpdate;
