<template>
  <div>
    <h2 id="page-heading" data-cy="AffectationHeading">
      <span v-text="$t('pfaApp.affectation.home.title')" id="affectation-heading">Affectations</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('pfaApp.affectation.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'AffectationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-affectation"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('pfaApp.affectation.home.createLabel')"> Create a new Affectation </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && affectations && affectations.length === 0">
      <span v-text="$t('pfaApp.affectation.home.notFound')">No affectations found</span>
    </div>
    <div class="table-responsive" v-if="affectations && affectations.length > 0">
      <table class="table table-striped" aria-describedby="affectations">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('pfaApp.affectation.dateDebut')">Date Debut</span></th>
            <th scope="row"><span v-text="$t('pfaApp.affectation.dateFin')">Date Fin</span></th>
            <th scope="row"><span v-text="$t('pfaApp.affectation.boitier')">Boitier</span></th>
            <th scope="row"><span v-text="$t('pfaApp.affectation.parcelle')">Parcelle</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="affectation in affectations" :key="affectation.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AffectationView', params: { affectationId: affectation.id } }">{{ affectation.id }}</router-link>
            </td>
            <td>{{ affectation.dateDebut }}</td>
            <td>{{ affectation.dateFin }}</td>
            <td>
              <div v-if="affectation.boitier">
                <router-link :to="{ name: 'BoitierView', params: { boitierId: affectation.boitier.id } }">{{
                  affectation.boitier.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="affectation.parcelle">
                <router-link :to="{ name: 'ParcelleView', params: { parcelleId: affectation.parcelle.id } }">{{
                  affectation.parcelle.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AffectationView', params: { affectationId: affectation.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AffectationEdit', params: { affectationId: affectation.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(affectation)"
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
        ><span id="pfaApp.affectation.delete.question" data-cy="affectationDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-affectation-heading" v-text="$t('pfaApp.affectation.delete.question', { id: removeId })">
          Are you sure you want to delete this Affectation?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-affectation"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeAffectation()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./affectation.component.ts"></script>
