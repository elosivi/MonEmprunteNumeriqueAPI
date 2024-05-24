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
import { IPrestationProfil } from 'app/shared/model/prestation-profil.model';
import { getEntity, updateEntity, createEntity, reset } from './prestation-profil.reducer';

export const PrestationProfilUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const profils = useAppSelector(state => state.profil.entities);
  const prestations = useAppSelector(state => state.prestation.entities);
  const prestationProfilEntity = useAppSelector(state => state.prestationProfil.entity);
  const loading = useAppSelector(state => state.prestationProfil.loading);
  const updating = useAppSelector(state => state.prestationProfil.updating);
  const updateSuccess = useAppSelector(state => state.prestationProfil.updateSuccess);

  const handleClose = () => {
    navigate('/prestation-profil' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProfils({}));
    dispatch(getPrestations({}));
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
    if (values.nbMoisPresta !== undefined && typeof values.nbMoisPresta !== 'number') {
      values.nbMoisPresta = Number(values.nbMoisPresta);
    }
    if (values.nbSemCongesEstime !== undefined && typeof values.nbSemCongesEstime !== 'number') {
      values.nbSemCongesEstime = Number(values.nbSemCongesEstime);
    }
    if (values.dureeHebdo !== undefined && typeof values.dureeHebdo !== 'number') {
      values.dureeHebdo = Number(values.dureeHebdo);
    }
    if (values.dureeTeletravail !== undefined && typeof values.dureeTeletravail !== 'number') {
      values.dureeTeletravail = Number(values.dureeTeletravail);
    }
    if (values.dureeReuAudio !== undefined && typeof values.dureeReuAudio !== 'number') {
      values.dureeReuAudio = Number(values.dureeReuAudio);
    }
    if (values.dureeReuVisio !== undefined && typeof values.dureeReuVisio !== 'number') {
      values.dureeReuVisio = Number(values.dureeReuVisio);
    }
    if (values.nbMailsSansPJ !== undefined && typeof values.nbMailsSansPJ !== 'number') {
      values.nbMailsSansPJ = Number(values.nbMailsSansPJ);
    }
    if (values.nbMailsAvecPJ !== undefined && typeof values.nbMailsAvecPJ !== 'number') {
      values.nbMailsAvecPJ = Number(values.nbMailsAvecPJ);
    }
    if (values.nbTerminaux !== undefined && typeof values.nbTerminaux !== 'number') {
      values.nbTerminaux = Number(values.nbTerminaux);
    }
    if (values.nbDeplacements !== undefined && typeof values.nbDeplacements !== 'number') {
      values.nbDeplacements = Number(values.nbDeplacements);
    }
    if (values.ecMensuelle !== undefined && typeof values.ecMensuelle !== 'number') {
      values.ecMensuelle = Number(values.ecMensuelle);
    }
    if (values.ecTotalePreta !== undefined && typeof values.ecTotalePreta !== 'number') {
      values.ecTotalePreta = Number(values.ecTotalePreta);
    }
    if (values.ecTransportMensuel !== undefined && typeof values.ecTransportMensuel !== 'number') {
      values.ecTransportMensuel = Number(values.ecTransportMensuel);
    }
    if (values.ecFabMateriel !== undefined && typeof values.ecFabMateriel !== 'number') {
      values.ecFabMateriel = Number(values.ecFabMateriel);
    }
    if (values.ecUtilMaterielMensuel !== undefined && typeof values.ecUtilMaterielMensuel !== 'number') {
      values.ecUtilMaterielMensuel = Number(values.ecUtilMaterielMensuel);
    }
    if (values.ecCommMensuel !== undefined && typeof values.ecCommMensuel !== 'number') {
      values.ecCommMensuel = Number(values.ecCommMensuel);
    }

    const entity = {
      ...prestationProfilEntity,
      ...values,
      profil: profils.find(it => it.id.toString() === values.profil?.toString()),
      prestations: mapIdList(values.prestations),
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
          ...prestationProfilEntity,
          profil: prestationProfilEntity?.profil?.id,
          prestations: prestationProfilEntity?.prestations?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="menapiApp.prestationProfil.home.createOrEditLabel" data-cy="PrestationProfilCreateUpdateHeading">
            <Translate contentKey="menapiApp.prestationProfil.home.createOrEditLabel">Create or edit a PrestationProfil</Translate>
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
                  id="prestation-profil-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('menapiApp.prestationProfil.nbMoisPresta')}
                id="prestation-profil-nbMoisPresta"
                name="nbMoisPresta"
                data-cy="nbMoisPresta"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.nbSemCongesEstime')}
                id="prestation-profil-nbSemCongesEstime"
                name="nbSemCongesEstime"
                data-cy="nbSemCongesEstime"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.dureeHebdo')}
                id="prestation-profil-dureeHebdo"
                name="dureeHebdo"
                data-cy="dureeHebdo"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.dureeTeletravail')}
                id="prestation-profil-dureeTeletravail"
                name="dureeTeletravail"
                data-cy="dureeTeletravail"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.dureeReuAudio')}
                id="prestation-profil-dureeReuAudio"
                name="dureeReuAudio"
                data-cy="dureeReuAudio"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.dureeReuVisio')}
                id="prestation-profil-dureeReuVisio"
                name="dureeReuVisio"
                data-cy="dureeReuVisio"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.nbMailsSansPJ')}
                id="prestation-profil-nbMailsSansPJ"
                name="nbMailsSansPJ"
                data-cy="nbMailsSansPJ"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.nbMailsAvecPJ')}
                id="prestation-profil-nbMailsAvecPJ"
                name="nbMailsAvecPJ"
                data-cy="nbMailsAvecPJ"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.veillePause')}
                id="prestation-profil-veillePause"
                name="veillePause"
                data-cy="veillePause"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.veilleSoir')}
                id="prestation-profil-veilleSoir"
                name="veilleSoir"
                data-cy="veilleSoir"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.veilleWeekend')}
                id="prestation-profil-veilleWeekend"
                name="veilleWeekend"
                data-cy="veilleWeekend"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.nbTerminaux')}
                id="prestation-profil-nbTerminaux"
                name="nbTerminaux"
                data-cy="nbTerminaux"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.nbDeplacements')}
                id="prestation-profil-nbDeplacements"
                name="nbDeplacements"
                data-cy="nbDeplacements"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.ecMensuelle')}
                id="prestation-profil-ecMensuelle"
                name="ecMensuelle"
                data-cy="ecMensuelle"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.ecTotalePreta')}
                id="prestation-profil-ecTotalePreta"
                name="ecTotalePreta"
                data-cy="ecTotalePreta"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.ecTransportMensuel')}
                id="prestation-profil-ecTransportMensuel"
                name="ecTransportMensuel"
                data-cy="ecTransportMensuel"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.ecFabMateriel')}
                id="prestation-profil-ecFabMateriel"
                name="ecFabMateriel"
                data-cy="ecFabMateriel"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.ecUtilMaterielMensuel')}
                id="prestation-profil-ecUtilMaterielMensuel"
                name="ecUtilMaterielMensuel"
                data-cy="ecUtilMaterielMensuel"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestationProfil.ecCommMensuel')}
                id="prestation-profil-ecCommMensuel"
                name="ecCommMensuel"
                data-cy="ecCommMensuel"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="prestation-profil-profil"
                name="profil"
                data-cy="profil"
                label={translate('menapiApp.prestationProfil.profil')}
                type="select"
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
                label={translate('menapiApp.prestationProfil.prestation')}
                id="prestation-profil-prestation"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/prestation-profil" replace color="info">
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

export default PrestationProfilUpdate;
