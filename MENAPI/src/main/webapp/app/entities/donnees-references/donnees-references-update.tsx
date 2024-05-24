import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUnite } from 'app/shared/model/unite.model';
import { getEntities as getUnites } from 'app/entities/unite/unite.reducer';
import { IDonneesReferences } from 'app/shared/model/donnees-references.model';
import { getEntity, updateEntity, createEntity, reset } from './donnees-references.reducer';

export const DonneesReferencesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const unites = useAppSelector(state => state.unite.entities);
  const donneesReferencesEntity = useAppSelector(state => state.donneesReferences.entity);
  const loading = useAppSelector(state => state.donneesReferences.loading);
  const updating = useAppSelector(state => state.donneesReferences.updating);
  const updateSuccess = useAppSelector(state => state.donneesReferences.updateSuccess);

  const handleClose = () => {
    navigate('/donnees-references' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUnites({}));
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
    if (values.donneeReference !== undefined && typeof values.donneeReference !== 'number') {
      values.donneeReference = Number(values.donneeReference);
    }

    const entity = {
      ...donneesReferencesEntity,
      ...values,
      unite: unites.find(it => it.id.toString() === values.unite?.toString()),
      temporalite: unites.find(it => it.id.toString() === values.temporalite?.toString()),
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
          ...donneesReferencesEntity,
          unite: donneesReferencesEntity?.unite?.id,
          temporalite: donneesReferencesEntity?.temporalite?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="menapiApp.donneesReferences.home.createOrEditLabel" data-cy="DonneesReferencesCreateUpdateHeading">
            <Translate contentKey="menapiApp.donneesReferences.home.createOrEditLabel">Create or edit a DonneesReferences</Translate>
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
                  id="donnees-references-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('menapiApp.donneesReferences.libelle')}
                id="donnees-references-libelle"
                name="libelle"
                data-cy="libelle"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 1, message: translate('entity.validation.minlength', { min: 1 }) },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('menapiApp.donneesReferences.donneeReference')}
                id="donnees-references-donneeReference"
                name="donneeReference"
                data-cy="donneeReference"
                type="text"
              />
              <ValidatedField
                id="donnees-references-unite"
                name="unite"
                data-cy="unite"
                label={translate('menapiApp.donneesReferences.unite')}
                type="select"
              >
                <option value="" key="0" />
                {unites
                  ? unites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="donnees-references-temporalite"
                name="temporalite"
                data-cy="temporalite"
                label={translate('menapiApp.donneesReferences.temporalite')}
                type="select"
              >
                <option value="" key="0" />
                {unites
                  ? unites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/donnees-references" replace color="info">
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

export default DonneesReferencesUpdate;
