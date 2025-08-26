import React, { useEffect } from 'react'
import { useAuth } from '../../context/AuthContext'

function ProfilePage() {
    const { token, email, userName } = useAuth();

    return (
        <div>Hello {email}! {userName}</div>
    )
}

export default ProfilePage