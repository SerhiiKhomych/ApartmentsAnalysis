import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

from sklearn.model_selection import train_test_split

from tensorflow.python.keras.models import Sequential
from tensorflow.python.keras.layers import Activation
from tensorflow.python.keras.layers import Dense
from tensorflow.python.keras.layers import Dropout
from tensorflow.python.keras.regularizers import l2
from tensorflow.python.keras.initializers import he_normal

from tensorflow.python.keras.layers.normalization import BatchNormalization
from tensorflow.python.keras.callbacks import LearningRateScheduler


def step_decay_schedule(initial_lr=1e-3, decay_factor=0.8, step_size=1500):
    def schedule(epoch):
        return initial_lr * (decay_factor ** np.floor(epoch / step_size))

    return LearningRateScheduler(schedule)


callback = step_decay_schedule()

fields = ['Material', 'TotalArea', 'RoomsNumber', 'FloorNumberCoeff', 'Distance', 'ApartmentAge', 'Price']
csv = pd.read_csv("../src/main/resources/k-means-3/cluster_1.csv", usecols=fields)

xs = csv[['Material', 'TotalArea', 'RoomsNumber', 'FloorNumberCoeff', 'Distance', 'ApartmentAge']].to_numpy()
ys = csv['Price'].to_numpy() / 1000

x_train, x_test, y_train, y_test = train_test_split(xs, ys, test_size=0.2)

model = Sequential()
model.add(Dense(128, input_dim=6))
# model.add(BatchNormalization())
# model.add(Dropout(0.3))

model.add(Dense(256))
# model.add(BatchNormalization())
model.add(Activation('relu'))
# model.add(Dropout(0.3))

model.add(Dense(256))
# model.add(BatchNormalization())
model.add(Activation('relu'))
# model.add(Dropout(0.3))

model.add(Dense(256))
# model.add(BatchNormalization())
model.add(Activation('relu'))
# model.add(Dropout(0.3))

# model.add(Dense(256))
# model.add(BatchNormalization())
# model.add(Activation('relu'))
# model.add(Dropout(0.3))

# model.add(Dense(256))
# model.add(BatchNormalization())
# model.add(Activation('relu'))
# model.add(Dropout(0.3))

model.add(Dense(1))
# model.add(BatchNormalization())
model.add(Activation('linear'))
model.compile(optimizer='adam', loss='mean_absolute_error', metrics=['mean_absolute_error'])

history = model.fit(x_train, y_train, epochs=60910, validation_split=0.1, callbacks=[callback])

# summarize history for loss
plt.plot(history.history['loss'])
plt.plot(history.history['val_loss'])
plt.title('model loss')
plt.ylabel('loss')
plt.xlabel('epoch')
plt.legend(['loss', 'val_loss'], loc='upper left')
plt.show()

home = np.array([['4', '48', '2', '0.6', '7521.69', '62']])
new_home = np.array([['1', '65', '2', '0.4285714', '1307.686', '1']])

print(model.predict(home))
print(model.predict(new_home))

y_predict = model.predict(x_test)

np.savetxt("foo.csv", np.column_stack((y_predict, y_test)) * 1000, delimiter=",", fmt='%d')
