import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { searchEntities, getEntities } from './prestation.reducer';

export const Prestation = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const prestationList = useAppSelector(state => state.prestation.entities);
  const loading = useAppSelector(state => state.prestation.loading);
  const totalItems = useAppSelector(state => state.prestation.totalItems);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    } else {
      dispatch(
        getEntities({
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    }
  };

  const startSearching = e => {
    if (search) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
      });
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, search]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="prestation-heading" data-cy="PrestationHeading">
        <Translate contentKey="menapiApp.prestation.home.title">Prestations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="menapiApp.prestation.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/prestation/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="menapiApp.prestation.home.createLabel">Create new Prestation</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('menapiApp.prestation.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {prestationList && prestationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="menapiApp.prestation.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nomPrestation')}>
                  <Translate contentKey="menapiApp.prestation.nomPrestation">Nom Prestation</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nomPrestation')} />
                </th>
                <th className="hand" onClick={sort('nomUtilisateur')}>
                  <Translate contentKey="menapiApp.prestation.nomUtilisateur">Nom Utilisateur</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nomUtilisateur')} />
                </th>
                <th className="hand" onClick={sort('nomMission')}>
                  <Translate contentKey="menapiApp.prestation.nomMission">Nom Mission</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nomMission')} />
                </th>
                <th className="hand" onClick={sort('nomClient')}>
                  <Translate contentKey="menapiApp.prestation.nomClient">Nom Client</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nomClient')} />
                </th>
                <th className="hand" onClick={sort('ecUnite')}>
                  <Translate contentKey="menapiApp.prestation.ecUnite">Ec Unite</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecUnite')} />
                </th>
                <th className="hand" onClick={sort('ecMensuelle')}>
                  <Translate contentKey="menapiApp.prestation.ecMensuelle">Ec Mensuelle</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecMensuelle')} />
                </th>
                <th className="hand" onClick={sort('ecTotale')}>
                  <Translate contentKey="menapiApp.prestation.ecTotale">Ec Totale</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecTotale')} />
                </th>
                <th className="hand" onClick={sort('ecTransportMensuel')}>
                  <Translate contentKey="menapiApp.prestation.ecTransportMensuel">Ec Transport Mensuel</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecTransportMensuel')} />
                </th>
                <th className="hand" onClick={sort('ecFabMateriel')}>
                  <Translate contentKey="menapiApp.prestation.ecFabMateriel">Ec Fab Materiel</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecFabMateriel')} />
                </th>
                <th className="hand" onClick={sort('ecUtilMaterielMensuel')}>
                  <Translate contentKey="menapiApp.prestation.ecUtilMaterielMensuel">Ec Util Materiel Mensuel</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecUtilMaterielMensuel')} />
                </th>
                <th className="hand" onClick={sort('ecCommMensuel')}>
                  <Translate contentKey="menapiApp.prestation.ecCommMensuel">Ec Comm Mensuel</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ecCommMensuel')} />
                </th>
                <th className="hand" onClick={sort('nbrProfils')}>
                  <Translate contentKey="menapiApp.prestation.nbrProfils">Nbr Profils</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nbrProfils')} />
                </th>
                <th className="hand" onClick={sort('dureeMois')}>
                  <Translate contentKey="menapiApp.prestation.dureeMois">Duree Mois</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dureeMois')} />
                </th>
                <th className="hand" onClick={sort('dateDebut')}>
                  <Translate contentKey="menapiApp.prestation.dateDebut">Date Debut</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dateDebut')} />
                </th>
                <th className="hand" onClick={sort('dateFin')}>
                  <Translate contentKey="menapiApp.prestation.dateFin">Date Fin</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dateFin')} />
                </th>
                <th className="hand" onClick={sort('typePresta')}>
                  <Translate contentKey="menapiApp.prestation.typePresta">Type Presta</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('typePresta')} />
                </th>
                <th className="hand" onClick={sort('lieupresta')}>
                  <Translate contentKey="menapiApp.prestation.lieupresta">Lieupresta</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lieupresta')} />
                </th>
                <th className="hand" onClick={sort('donneesSaisies')}>
                  <Translate contentKey="menapiApp.prestation.donneesSaisies">Donnees Saisies</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('donneesSaisies')} />
                </th>
                <th className="hand" onClick={sort('donneesReperes')}>
                  <Translate contentKey="menapiApp.prestation.donneesReperes">Donnees Reperes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('donneesReperes')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {prestationList.map((prestation, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/prestation/${prestation.id}`} color="link" size="sm">
                      {prestation.id}
                    </Button>
                  </td>
                  <td>{prestation.nomPrestation}</td>
                  <td>{prestation.nomUtilisateur}</td>
                  <td>{prestation.nomMission}</td>
                  <td>{prestation.nomClient}</td>
                  <td>{prestation.ecUnite}</td>
                  <td>{prestation.ecMensuelle}</td>
                  <td>{prestation.ecTotale}</td>
                  <td>{prestation.ecTransportMensuel}</td>
                  <td>{prestation.ecFabMateriel}</td>
                  <td>{prestation.ecUtilMaterielMensuel}</td>
                  <td>{prestation.ecCommMensuel}</td>
                  <td>{prestation.nbrProfils}</td>
                  <td>{prestation.dureeMois}</td>
                  <td>
                    {prestation.dateDebut ? <TextFormat type="date" value={prestation.dateDebut} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {prestation.dateFin ? <TextFormat type="date" value={prestation.dateFin} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    <Translate contentKey={`menapiApp.TypePresta.${prestation.typePresta}`} />
                  </td>
                  <td>
                    <Translate contentKey={`menapiApp.LieuPresta.${prestation.lieupresta}`} />
                  </td>
                  <td>{prestation.donneesSaisies ? 'true' : 'false'}</td>
                  <td>{prestation.donneesReperes ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/prestation/${prestation.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/prestation/${prestation.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/prestation/${prestation.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="menapiApp.prestation.home.notFound">No Prestations found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={prestationList && prestationList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Prestation;
