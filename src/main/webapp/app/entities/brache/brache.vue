<template>
  <div>
    <h2 id="page-heading" data-cy="BracheHeading">
      <span v-text="$t('pfaApp.brache.home.title')" id="brache-heading">Braches</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('pfaApp.brache.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'BracheCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-brache"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('pfaApp.brache.home.createLabel')"> Create a new Brache </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && braches && braches.length === 0">
      <span v-text="$t('pfaApp.brache.home.notFound')">No braches found</span>
    </div>
    <div class="table-responsive" v-if="braches && braches.length > 0">
      <table class="table table-striped" aria-describedby="braches">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('pfaApp.brache.branche')">Branche</span></th>
            <th scope="row"><span v-text="$t('pfaApp.brache.capteur')">Capteur</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="brache in braches" :key="brache.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'BracheView', params: { bracheId: brache.id } }">{{ brache.id }}</router-link>
            </td>
            <td>{{ brache.branche }}</td>
            <td>
              <span v-for="(capteur, i) in brache.capteurs" :key="capteur.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'CapteurView', params: { capteurId: capteur.id } }">{{
                  capteur.id
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'BracheView', params: { bracheId: brache.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'BracheEdit', params: { bracheId: brache.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(brache)"
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
        ><span id="pfaApp.brache.delete.question" data-cy="bracheDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-brache-heading" v-text="$t('pfaApp.brache.delete.question', { id: removeId })">
          Are you sure you want to delete this Brache?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-brache"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeBrache()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./brache.component.ts"></script>
