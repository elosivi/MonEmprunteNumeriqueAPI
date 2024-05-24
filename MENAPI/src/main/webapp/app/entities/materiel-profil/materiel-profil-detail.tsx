import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './materiel-profil.reducer';

export const MaterielProfilDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const materielProfilEntity = useAppSelector(state => state.materielProfil.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="materielProfilDetailsHeading">
          <Translate contentKey="menapiApp.materielProfil.detail.title">MaterielProfil</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{materielProfilEntity.id}</dd>
          <dt>
            <span id="dureeHebdo">
              <Translate contentKey="menapiApp.materielProfil.dureeHebdo">Duree Hebdo</Translate>
            </span>
          </dt>
          <dd>{materielProfilEntity.dureeHebdo}</dd>
          <dt>
            <span id="estNeuf">
              <Translate contentKey="menapiApp.materielProfil.estNeuf">Est Neuf</Translate>
            </span>
          </dt>
          <dd>{materielProfilEntity.estNeuf ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="menapiApp.materielProfil.profil">Profil</Translate>
          </dt>
          <dd>
            {materielProfilEntity.profils
              ? materielProfilEntity.profils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {materielProfilEntity.profils && i === materielProfilEntity.profils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="menapiApp.materielProfil.prestation">Prestation</Translate>
          </dt>
          <dd>
            {materielProfilEntity.prestations
              ? materielProfilEntity.prestations.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {materielProfilEntity.prestations && i === materielProfilEntity.prestations.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="menapiApp.materielProfil.materiel">Materiel</Translate>
          </dt>
          <dd>
            {materielProfilEntity.materiels
              ? materielProfilEntity.materiels.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {materielProfilEntity.materiels && i === materielProfilEntity.materiels.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/materiel-profil" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/materiel-profil/${materielProfilEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MaterielProfilDetail;
