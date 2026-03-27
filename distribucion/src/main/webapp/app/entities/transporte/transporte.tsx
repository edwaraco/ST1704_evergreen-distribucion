import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './transporte.reducer';

export const Transporte = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const transporteList = useAppSelector(state => state.transporte.entities);
  const loading = useAppSelector(state => state.transporte.loading);
  const links = useAppSelector(state => state.transporte.links);
  const updateSuccess = useAppSelector(state => state.transporte.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="transporte-heading" data-cy="TransporteHeading">
        <Translate contentKey="evergreenApp.transporte.home.title">Transportes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="evergreenApp.transporte.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/transporte/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="evergreenApp.transporte.home.createLabel">Create new Transporte</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={transporteList ? transporteList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {transporteList && transporteList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="evergreenApp.transporte.id">ID</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('identificador')}>
                    <Translate contentKey="evergreenApp.transporte.identificador">Identificador</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('identificador')} />
                  </th>
                  <th className="hand" onClick={sort('tipoTransporte')}>
                    <Translate contentKey="evergreenApp.transporte.tipoTransporte">Tipo Transporte</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('tipoTransporte')} />
                  </th>
                  <th className="hand" onClick={sort('matricula')}>
                    <Translate contentKey="evergreenApp.transporte.matricula">Matricula</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('matricula')} />
                  </th>
                  <th className="hand" onClick={sort('capacidadKg')}>
                    <Translate contentKey="evergreenApp.transporte.capacidadKg">Capacidad Kg</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('capacidadKg')} />
                  </th>
                  <th className="hand" onClick={sort('capacidadM3')}>
                    <Translate contentKey="evergreenApp.transporte.capacidadM3">Capacidad M 3</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('capacidadM3')} />
                  </th>
                  <th className="hand" onClick={sort('estado')}>
                    <Translate contentKey="evergreenApp.transporte.estado">Estado</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('estado')} />
                  </th>
                  <th className="hand" onClick={sort('fechaAsignacion')}>
                    <Translate contentKey="evergreenApp.transporte.fechaAsignacion">Fecha Asignacion</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('fechaAsignacion')} />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {transporteList.map((transporte, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/transporte/${transporte.id}`} color="link" size="sm">
                        {transporte.id}
                      </Button>
                    </td>
                    <td>{transporte.identificador}</td>
                    <td>
                      <Translate contentKey={`evergreenApp.TipoTransporte.${transporte.tipoTransporte}`} />
                    </td>
                    <td>{transporte.matricula}</td>
                    <td>{transporte.capacidadKg}</td>
                    <td>{transporte.capacidadM3}</td>
                    <td>{transporte.estado}</td>
                    <td>
                      {transporte.fechaAsignacion ? (
                        <TextFormat type="date" value={transporte.fechaAsignacion} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/transporte/${transporte.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/transporte/${transporte.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/transporte/${transporte.id}/delete`)}
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
                <Translate contentKey="evergreenApp.transporte.home.notFound">No Transportes found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Transporte;
