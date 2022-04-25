<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pfaApp.boitier.home.createOrEditLabel"
          data-cy="BoitierCreateUpdateHeading"
          v-text="$t('pfaApp.boitier.home.createOrEditLabel')"
        >
          Create or edit a Boitier
        </h2>
        <div>
          <div class="form-group" v-if="boitier.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="boitier.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.boitier.reference')" for="boitier-reference">Reference</label>
            <input
              type="text"
              class="form-control"
              name="reference"
              id="boitier-reference"
              data-cy="reference"
              :class="{ valid: !$v.boitier.reference.$invalid, invalid: $v.boitier.reference.$invalid }"
              v-model="$v.boitier.reference.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.boitier.type')" for="boitier-type">Type</label>
            <input
              type="text"
              class="form-control"
              name="type"
              id="boitier-type"
              data-cy="type"
              :class="{ valid: !$v.boitier.type.$invalid, invalid: $v.boitier.type.$invalid }"
              v-model="$v.boitier.type.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.boitier.alerte')" for="boitier-alerte">Alerte</label>
            <select class="form-control" id="boitier-alerte" data-cy="alerte" name="alerte" v-model="boitier.alerte">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="boitier.alerte && alerteOption.id === boitier.alerte.id ? boitier.alerte : alerteOption"
                v-for="alerteOption in alertes"
                :key="alerteOption.id"
              >
                {{ alerteOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="$t('pfaApp.boitier.brache')" for="boitier-brache">Brache</label>
            <select
              class="form-control"
              id="boitier-braches"
              data-cy="brache"
              multiple
              name="brache"
              v-if="boitier.braches !== undefined"
              v-model="boitier.braches"
            >
              <option v-bind:value="getSelected(boitier.braches, bracheOption)" v-for="bracheOption in braches" :key="bracheOption.id">
                {{ bracheOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.boitier.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./boitier-update.component.ts"></script>
