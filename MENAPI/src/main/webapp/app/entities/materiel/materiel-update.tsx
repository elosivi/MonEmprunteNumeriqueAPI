import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMaterielProfil } from 'app/shared/model/materiel-profil.model';
import { getEntities as getMaterielProfils } from 'app/entities/materiel-profil/materiel-profil.reducer';
import { IMateriel } from 'app/shared/model/materiel.model';
import { getEntity, updateEntity, createEntity, reset } from './materiel.reducer';

export const MaterielUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const materielProfils = useAppSelector(state => state.materielProfil.entities);
  const materielEntity = useAppSelector(state => state.materiel.entity);
  const loading = useAppSelector(state => state.materiel.loading);
  const updating = useAppSelector(state => state.materiel.updating);
  const updateSuccess = useAppSelector(state => state.materiel.updateSuccess);

  const handleClose = () => {
    navigate('/materiel' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    if (values.feVeille !== undefined && typeof values.feVeille !== 'number') {
      values.feVeille = Number(values.feVeille);
    }

    const entity = {
      ...materielEntity,
      ...values,
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
          ...materielEntity,
          materielProfils: materielEntity?.materielProfils?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="menapiApp.materiel.home.createOrEditLabel" data-cy="MaterielCreateUpdateHeading">
            <Translate contentKey="menapiApp.materiel.home.createOrEditLabel">Create or edit a Materiel</Translate>
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
                  id="materiel-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('menapiApp.materiel.libelle')}
                id="materiel-libelle"
                name="libelle"
                data-cy="libelle"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('menapiApp.materiel.feVeille')}
                id="materiel-feVeille"
                name="feVeille"
                data-cy="feVeille"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.materiel.materielProfil')}
                id="materiel-materielProfil"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/materiel" replace color="info">
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

export default MaterielUpdate;
