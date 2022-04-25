<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="pfaApp.alerte.home.createOrEditLabel"
          data-cy="AlerteCreateUpdateHeading"
          v-text="$t('pfaApp.alerte.home.createOrEditLabel')"
        >
          Create or edit a Alerte
        </h2>
        <div>
          <div class="form-group" v-if="alerte.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="alerte.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.alerte.humidite')" for="alerte-humidite">Humidite</label>
            <input
              type="number"
              class="form-control"
              name="humidite"
              id="alerte-humidite"
              data-cy="humidite"
              :class="{ valid: !$v.alerte.humidite.$invalid, invalid: $v.alerte.humidite.$invalid }"
              v-model.number="$v.alerte.humidite.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.alerte.temperature')" for="alerte-temperature">Temperature</label>
            <input
              type="number"
              class="form-control"
              name="temperature"
              id="alerte-temperature"
              data-cy="temperature"
              :class="{ valid: !$v.alerte.temperature.$invalid, invalid: $v.alerte.temperature.$invalid }"
              v-model.number="$v.alerte.temperature.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.alerte.luminosite')" for="alerte-luminosite">Luminosite</label>
            <input
              type="number"
              class="form-control"
              name="luminosite"
              id="alerte-luminosite"
              data-cy="luminosite"
              :class="{ valid: !$v.alerte.luminosite.$invalid, invalid: $v.alerte.luminosite.$invalid }"
              v-model.number="$v.alerte.luminosite.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('pfaApp.alerte.parcelle')" for="alerte-parcelle">Parcelle</label>
            <select class="form-control" id="alerte-parcelle" data-cy="parcelle" name="parcelle" v-model="alerte.parcelle">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="alerte.parcelle && parcelleOption.id === alerte.parcelle.id ? alerte.parcelle : parcelleOption"
                v-for="parcelleOption in parcelles"
                :key="parcelleOption.id"
              >
                {{ parcelleOption.id }}
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
            :disabled="$v.alerte.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./alerte-update.component.ts"></script>
