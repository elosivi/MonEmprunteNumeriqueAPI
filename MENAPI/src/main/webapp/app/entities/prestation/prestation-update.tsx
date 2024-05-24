import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPrestationProfil } from 'app/shared/model/prestation-profil.model';
import { getEntities as getPrestationProfils } from 'app/entities/prestation-profil/prestation-profil.reducer';
import { ITransportProfil } from 'app/shared/model/transport-profil.model';
import { getEntities as getTransportProfils } from 'app/entities/transport-profil/transport-profil.reducer';
import { IDeplacementProfil } from 'app/shared/model/deplacement-profil.model';
import { getEntities as getDeplacementProfils } from 'app/entities/deplacement-profil/deplacement-profil.reducer';
import { IMaterielProfil } from 'app/shared/model/materiel-profil.model';
import { getEntities as getMaterielProfils } from 'app/entities/materiel-profil/materiel-profil.reducer';
import { IPrestation } from 'app/shared/model/prestation.model';
import { TypePresta } from 'app/shared/model/enumerations/type-presta.model';
import { LieuPresta } from 'app/shared/model/enumerations/lieu-presta.model';
import { getEntity, updateEntity, createEntity, reset } from './prestation.reducer';

export const PrestationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const prestationProfils = useAppSelector(state => state.prestationProfil.entities);
  const transportProfils = useAppSelector(state => state.transportProfil.entities);
  const deplacementProfils = useAppSelector(state => state.deplacementProfil.entities);
  const materielProfils = useAppSelector(state => state.materielProfil.entities);
  const prestationEntity = useAppSelector(state => state.prestation.entity);
  const loading = useAppSelector(state => state.prestation.loading);
  const updating = useAppSelector(state => state.prestation.updating);
  const updateSuccess = useAppSelector(state => state.prestation.updateSuccess);
  const typePrestaValues = Object.keys(TypePresta);
  const lieuPrestaValues = Object.keys(LieuPresta);

  const handleClose = () => {
    navigate('/prestation' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPrestationProfils({}));
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
    if (values.ecMensuelle !== undefined && typeof values.ecMensuelle !== 'number') {
      values.ecMensuelle = Number(values.ecMensuelle);
    }
    if (values.ecTotale !== undefined && typeof values.ecTotale !== 'number') {
      values.ecTotale = Number(values.ecTotale);
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
    if (values.nbrProfils !== undefined && typeof values.nbrProfils !== 'number') {
      values.nbrProfils = Number(values.nbrProfils);
    }
    if (values.dureeMois !== undefined && typeof values.dureeMois !== 'number') {
      values.dureeMois = Number(values.dureeMois);
    }

    const entity = {
      ...prestationEntity,
      ...values,
      prestationProfils: mapIdList(values.prestationProfils),
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
          typePresta: 'REG',
          lieupresta: 'CLI',
          ...prestationEntity,
          prestationProfils: prestationEntity?.prestationProfils?.map(e => e.id.toString()),
          transportProfils: prestationEntity?.transportProfils?.map(e => e.id.toString()),
          deplacementProfils: prestationEntity?.deplacementProfils?.map(e => e.id.toString()),
          materielProfils: prestationEntity?.materielProfils?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="menapiApp.prestation.home.createOrEditLabel" data-cy="PrestationCreateUpdateHeading">
            <Translate contentKey="menapiApp.prestation.home.createOrEditLabel">Create or edit a Prestation</Translate>
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
                  id="prestation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('menapiApp.prestation.nomPrestation')}
                id="prestation-nomPrestation"
                name="nomPrestation"
                data-cy="nomPrestation"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.nomUtilisateur')}
                id="prestation-nomUtilisateur"
                name="nomUtilisateur"
                data-cy="nomUtilisateur"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                  maxLength: { value: 150, message: translate('entity.validation.maxlength', { max: 150 }) },
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.nomMission')}
                id="prestation-nomMission"
                name="nomMission"
                data-cy="nomMission"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                  maxLength: { value: 150, message: translate('entity.validation.maxlength', { max: 150 }) },
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.nomClient')}
                id="prestation-nomClient"
                name="nomClient"
                data-cy="nomClient"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                  maxLength: { value: 150, message: translate('entity.validation.maxlength', { max: 150 }) },
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.ecUnite')}
                id="prestation-ecUnite"
                name="ecUnite"
                data-cy="ecUnite"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 1, message: translate('entity.validation.minlength', { min: 1 }) },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.ecMensuelle')}
                id="prestation-ecMensuelle"
                name="ecMensuelle"
                data-cy="ecMensuelle"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.ecTotale')}
                id="prestation-ecTotale"
                name="ecTotale"
                data-cy="ecTotale"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.ecTransportMensuel')}
                id="prestation-ecTransportMensuel"
                name="ecTransportMensuel"
                data-cy="ecTransportMensuel"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.ecFabMateriel')}
                id="prestation-ecFabMateriel"
                name="ecFabMateriel"
                data-cy="ecFabMateriel"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.ecUtilMaterielMensuel')}
                id="prestation-ecUtilMaterielMensuel"
                name="ecUtilMaterielMensuel"
                data-cy="ecUtilMaterielMensuel"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.ecCommMensuel')}
                id="prestation-ecCommMensuel"
                name="ecCommMensuel"
                data-cy="ecCommMensuel"
                type="text"
                validate={{
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.nbrProfils')}
                id="prestation-nbrProfils"
                name="nbrProfils"
                data-cy="nbrProfils"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.dureeMois')}
                id="prestation-dureeMois"
                name="dureeMois"
                data-cy="dureeMois"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('menapiApp.prestation.dateDebut')}
                id="prestation-dateDebut"
                name="dateDebut"
                data-cy="dateDebut"
                type="date"
              />
              <ValidatedField
                label={translate('menapiApp.prestation.dateFin')}
                id="prestation-dateFin"
                name="dateFin"
                data-cy="dateFin"
                type="date"
              />
              <ValidatedField
                label={translate('menapiApp.prestation.typePresta')}
                id="prestation-typePresta"
                name="typePresta"
                data-cy="typePresta"
                type="select"
              >
                {typePrestaValues.map(typePresta => (
                  <option value={typePresta} key={typePresta}>
                    {translate('menapiApp.TypePresta.' + typePresta)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('menapiApp.prestation.lieupresta')}
                id="prestation-lieupresta"
                name="lieupresta"
                data-cy="lieupresta"
                type="select"
              >
                {lieuPrestaValues.map(lieuPresta => (
                  <option value={lieuPresta} key={lieuPresta}>
                    {translate('menapiApp.LieuPresta.' + lieuPresta)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('menapiApp.prestation.donneesSaisies')}
                id="prestation-donneesSaisies"
                name="donneesSaisies"
                data-cy="donneesSaisies"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('menapiApp.prestation.donneesReperes')}
                id="prestation-donneesReperes"
                name="donneesReperes"
                data-cy="donneesReperes"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('menapiApp.prestation.prestationProfil')}
                id="prestation-prestationProfil"
                data-cy="prestationProfil"
                type="select"
                multiple
                name="prestationProfils"
              >
                <option value="" key="0" />
                {prestationProfils
                  ? prestationProfils.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('menapiApp.prestation.transportProfil')}
                id="prestation-transportProfil"
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
                label={translate('menapiApp.prestation.deplacementProfil')}
                id="prestation-deplacementProfil"
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
                label={translate('menapiApp.prestation.materielProfil')}
                id="prestation-materielProfil"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/prestation" replace color="info">
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

export default PrestationUpdate;
