<template>
  <div>
    <h2 id="page-heading" data-cy="ParcelleHeading">
      <span v-text="$t('pfaApp.parcelle.home.title')" id="parcelle-heading">Parcelles</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('pfaApp.parcelle.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ParcelleCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-parcelle"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('pfaApp.parcelle.home.createLabel')"> Create a new Parcelle </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && parcelles && parcelles.length === 0">
      <span v-text="$t('pfaApp.parcelle.home.notFound')">No parcelles found</span>
    </div>
    <div class="table-responsive" v-if="parcelles && parcelles.length > 0">
      <table class="table table-striped" aria-describedby="parcelles">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('pfaApp.parcelle.surface')">Surface</span></th>
            <th scope="row"><span v-text="$t('pfaApp.parcelle.photo')">Photo</span></th>
            <th scope="row"><span v-text="$t('pfaApp.parcelle.ferm')">Ferm</span></th>
            <th scope="row"><span v-text="$t('pfaApp.parcelle.plantage')">Plantage</span></th>
            <th scope="row"><span v-text="$t('pfaApp.parcelle.typeSol')">Type Sol</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="parcelle in parcelles" :key="parcelle.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ParcelleView', params: { parcelleId: parcelle.id } }">{{ parcelle.id }}</router-link>
            </td>
            <td>{{ parcelle.surface }}</td>
            <td>{{ parcelle.photo }}</td>
            <td>
              <span v-for="(ferm, i) in parcelle.ferms" :key="ferm.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'FermView', params: { fermId: ferm.id } }">{{ ferm.id }}</router-link>
              </span>
            </td>
            <td>
              <span v-for="(plantage, i) in parcelle.plantages" :key="plantage.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'PlantageView', params: { plantageId: plantage.id } }">{{
                  plantage.id
                }}</router-link>
              </span>
            </td>
            <td>
              <div v-if="parcelle.typeSol">
                <router-link :to="{ name: 'TypeSolView', params: { typeSolId: parcelle.typeSol.id } }">{{
                  parcelle.typeSol.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ParcelleView', params: { parcelleId: parcelle.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ParcelleEdit', params: { parcelleId: parcelle.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(parcelle)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="pfaApp.parcelle.delete.question" data-cy="parcelleDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-parcelle-heading" v-text="$t('pfaApp.parcelle.delete.question', { id: removeId })">
          Are you sure you want to delete this Parcelle?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-parcelle"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeParcelle()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./parcelle.component.ts"></script>
