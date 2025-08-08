package com.luisma.pactype.views.game.data


enum class MoveType {
    Right {
        override fun counterMove() = Left
    },
    Down {
        override fun counterMove() = Up
    },
    Left {
        override fun counterMove() = Right
    },
    Up {
        override fun counterMove() = Down
    };

    abstract fun counterMove() : MoveType
}
