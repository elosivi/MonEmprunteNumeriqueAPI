import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './deplacement-profil.reducer';

export const DeplacementProfilDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const deplacementProfilEntity = useAppSelector(state => state.deplacementProfil.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="deplacementProfilDetailsHeading">
          <Translate contentKey="menapiApp.deplacementProfil.detail.title">DeplacementProfil</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{deplacementProfilEntity.id}</dd>
          <dt>
            <span id="nbDeplacement">
              <Translate contentKey="menapiApp.deplacementProfil.nbDeplacement">Nb Deplacement</Translate>
            </span>
          </dt>
          <dd>{deplacementProfilEntity.nbDeplacement}</dd>
          <dt>
            <span id="kmPresta">
              <Translate contentKey="menapiApp.deplacementProfil.kmPresta">Km Presta</Translate>
            </span>
          </dt>
          <dd>{deplacementProfilEntity.kmPresta}</dd>
          <dt>
            <Translate contentKey="menapiApp.deplacementProfil.profil">Profil</Translate>
          </dt>
          <dd>
            {deplacementProfilEntity.profils
              ? deplacementProfilEntity.profils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {deplacementProfilEntity.profils && i === deplacementProfilEntity.profils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="menapiApp.deplacementProfil.prestation">Prestation</Translate>
          </dt>
          <dd>
            {deplacementProfilEntity.prestations
              ? deplacementProfilEntity.prestations.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {deplacementProfilEntity.prestations && i === deplacementProfilEntity.prestations.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="menapiApp.deplacementProfil.transport">Transport</Translate>
          </dt>
          <dd>
            {deplacementProfilEntity.transports
              ? deplacementProfilEntity.transports.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {deplacementProfilEntity.transports && i === deplacementProfilEntity.transports.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/deplacement-profil" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/deplacement-profil/${deplacementProfilEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DeplacementProfilDetail;
