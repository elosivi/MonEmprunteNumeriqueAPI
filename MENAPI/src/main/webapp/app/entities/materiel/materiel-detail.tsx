import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './materiel.reducer';

export const MaterielDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const materielEntity = useAppSelector(state => state.materiel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="materielDetailsHeading">
          <Translate contentKey="menapiApp.materiel.detail.title">Materiel</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{materielEntity.id}</dd>
          <dt>
            <span id="libelle">
              <Translate contentKey="menapiApp.materiel.libelle">Libelle</Translate>
            </span>
          </dt>
          <dd>{materielEntity.libelle}</dd>
          <dt>
            <span id="feVeille">
              <Translate contentKey="menapiApp.materiel.feVeille">Fe Veille</Translate>
            </span>
          </dt>
          <dd>{materielEntity.feVeille}</dd>
          <dt>
            <Translate contentKey="menapiApp.materiel.materielProfil">Materiel Profil</Translate>
          </dt>
          <dd>
            {materielEntity.materielProfils
              ? materielEntity.materielProfils.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {materielEntity.materielProfils && i === materielEntity.materielProfils.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/materiel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/materiel/${materielEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MaterielDetail;
