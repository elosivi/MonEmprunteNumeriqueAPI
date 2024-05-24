import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './prestation-profil.reducer';

export const PrestationProfilDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const prestationProfilEntity = useAppSelector(state => state.prestationProfil.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="prestationProfilDetailsHeading">
          <Translate contentKey="menapiApp.prestationProfil.detail.title">PrestationProfil</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.id}</dd>
          <dt>
            <span id="nbMoisPresta">
              <Translate contentKey="menapiApp.prestationProfil.nbMoisPresta">Nb Mois Presta</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.nbMoisPresta}</dd>
          <dt>
            <span id="nbSemCongesEstime">
              <Translate contentKey="menapiApp.prestationProfil.nbSemCongesEstime">Nb Sem Conges Estime</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.nbSemCongesEstime}</dd>
          <dt>
            <span id="dureeHebdo">
              <Translate contentKey="menapiApp.prestationProfil.dureeHebdo">Duree Hebdo</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.dureeHebdo}</dd>
          <dt>
            <span id="dureeTeletravail">
              <Translate contentKey="menapiApp.prestationProfil.dureeTeletravail">Duree Teletravail</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.dureeTeletravail}</dd>
          <dt>
            <span id="dureeReuAudio">
              <Translate contentKey="menapiApp.prestationProfil.dureeReuAudio">Duree Reu Audio</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.dureeReuAudio}</dd>
          <dt>
            <span id="dureeReuVisio">
              <Translate contentKey="menapiApp.prestationProfil.dureeReuVisio">Duree Reu Visio</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.dureeReuVisio}</dd>
          <dt>
            <span id="nbMailsSansPJ">
              <Translate contentKey="menapiApp.prestationProfil.nbMailsSansPJ">Nb Mails Sans PJ</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.nbMailsSansPJ}</dd>
          <dt>
            <span id="nbMailsAvecPJ">
              <Translate contentKey="menapiApp.prestationProfil.nbMailsAvecPJ">Nb Mails Avec PJ</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.nbMailsAvecPJ}</dd>
          <dt>
            <span id="veillePause">
              <Translate contentKey="menapiApp.prestationProfil.veillePause">Veille Pause</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.veillePause ? 'true' : 'false'}</dd>
          <dt>
            <span id="veilleSoir">
              <Translate contentKey="menapiApp.prestationProfil.veilleSoir">Veille Soir</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.veilleSoir ? 'true' : 'false'}</dd>
          <dt>
            <span id="veilleWeekend">
              <Translate contentKey="menapiApp.prestationProfil.veilleWeekend">Veille Weekend</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.veilleWeekend ? 'true' : 'false'}</dd>
          <dt>
            <span id="nbTerminaux">
              <Translate contentKey="menapiApp.prestationProfil.nbTerminaux">Nb Terminaux</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.nbTerminaux}</dd>
          <dt>
            <span id="nbDeplacements">
              <Translate contentKey="menapiApp.prestationProfil.nbDeplacements">Nb Deplacements</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.nbDeplacements}</dd>
          <dt>
            <span id="ecMensuelle">
              <Translate contentKey="menapiApp.prestationProfil.ecMensuelle">Ec Mensuelle</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.ecMensuelle}</dd>
          <dt>
            <span id="ecTotalePreta">
              <Translate contentKey="menapiApp.prestationProfil.ecTotalePreta">Ec Totale Preta</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.ecTotalePreta}</dd>
          <dt>
            <span id="ecTransportMensuel">
              <Translate contentKey="menapiApp.prestationProfil.ecTransportMensuel">Ec Transport Mensuel</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.ecTransportMensuel}</dd>
          <dt>
            <span id="ecFabMateriel">
              <Translate contentKey="menapiApp.prestationProfil.ecFabMateriel">Ec Fab Materiel</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.ecFabMateriel}</dd>
          <dt>
            <span id="ecUtilMaterielMensuel">
              <Translate contentKey="menapiApp.prestationProfil.ecUtilMaterielMensuel">Ec Util Materiel Mensuel</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.ecUtilMaterielMensuel}</dd>
          <dt>
            <span id="ecCommMensuel">
              <Translate contentKey="menapiApp.prestationProfil.ecCommMensuel">Ec Comm Mensuel</Translate>
            </span>
          </dt>
          <dd>{prestationProfilEntity.ecCommMensuel}</dd>
          <dt>
            <Translate contentKey="menapiApp.prestationProfil.profil">Profil</Translate>
          </dt>
          <dd>{prestationProfilEntity.profil ? prestationProfilEntity.profil.id : ''}</dd>
          <dt>
            <Translate contentKey="menapiApp.prestationProfil.prestation">Prestation</Translate>
          </dt>
          <dd>
            {prestationProfilEntity.prestations
              ? prestationProfilEntity.prestations.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {prestationProfilEntity.prestations && i === prestationProfilEntity.prestations.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/prestation-profil" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/prestation-profil/${prestationProfilEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PrestationProfilDetail;
