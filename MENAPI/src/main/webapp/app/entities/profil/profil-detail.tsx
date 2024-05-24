import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './profil.reducer';

export const ProfilDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const profilEntity = useAppSelector(state => state.profil.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="profilDetailsHeading">
          <Translate contentKey="menapiApp.profil.detail.title">Profil</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{profilEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="menapiApp.profil.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{profilEntity.nom}</dd>
          <dt>
            <span id="prenom">
              <Translate contentKey="menapiApp.profil.prenom">Prenom</Translate>
            </span>
          </dt>
          <dd>{profilEntity.prenom}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="menapiApp.profil.email">Email</Translate>
            </span>
          </dt>
          <dd>{profilEntity.email}</dd>
          <dt>
            <Translate contentKey="menapiApp.profil.fonction">Fonction</Translate>
          </dt>
          <dd>{profilEntity.fonction ? profilEntity.fonction.id : ''}</dd>
          <dt>
            <Translate contentKey="menapiApp.profil.transportProfil">Transport Profil</Translate>
          </dt>
          <dd>
            {profilEntity.transportProfils
              ? profilEntity.transportProfils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {profilEntity.transportProfils && i === profilEntity.transportProfils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="menapiApp.profil.deplacementProfil">Deplacement Profil</Translate>
          </dt>
          <dd>
            {profilEntity.deplacementProfils
              ? profilEntity.deplacementProfils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {profilEntity.deplacementProfils && i === profilEntity.deplacementProfils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="menapiApp.profil.materielProfil">Materiel Profil</Translate>
          </dt>
          <dd>
            {profilEntity.materielProfils
              ? profilEntity.materielProfils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {profilEntity.materielProfils && i === profilEntity.materielProfils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/profil" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/profil/${profilEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProfilDetail;
